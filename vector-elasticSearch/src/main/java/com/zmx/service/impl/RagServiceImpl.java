package com.zmx.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.*;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import com.alibaba.cloud.ai.advisor.RetrievalRerankAdvisor;
import com.alibaba.cloud.ai.model.RerankModel;
import com.zmx.service.RagService;
import io.micrometer.common.util.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.elasticsearch.autoconfigure.ElasticsearchVectorStoreProperties;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RagServiceImpl implements RagService {

    private static final String textField = "content";
    private static final String vectorField = "embedding";

    @Value("classpath:/data/航班状态.pdf")
    private Resource springAiResources;

    @Value("classpath:/propmpt/system-qa.txt")
    private Resource systemPrompt;

    private final VectorStore vectorStore;
    private final ChatModel chatModel;
    private final RerankModel rerankModel;

    private final ElasticsearchClient elasticsearchClient;

    private final ElasticsearchVectorStoreProperties options;

    //构造方法
    public RagServiceImpl(VectorStore vectorStore, ChatModel chatModel, RerankModel rerankModel,
                      ElasticsearchClient elasticsearchClient, ElasticsearchVectorStoreProperties options) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
        this.rerankModel = rerankModel;
        this.elasticsearchClient = elasticsearchClient;
        this.options = options;
    }

    @Override
    public void importDocuments() {
        //读取pdf文件
        PagePdfDocumentReader reader = new PagePdfDocumentReader(springAiResources);
        List<Document> documents = reader.read();
        //分隔
        List<Document> splitDocuments = new TokenTextSplitter().apply(documents);
        //创建向量存储索引库
        createIndexIfNotExists();
        //嵌入向量存储
        vectorStore.add(splitDocuments);
    }

    @Override
    public Flux<ChatResponse> retrieve(String message) {
        SearchRequest searchRequest = SearchRequest.builder()
                .topK(5)
                .similarityThresholdAll()
                .filterExpression(new FilterExpressionBuilder().eq(textField, message).build())
                .build();
        //搜索向量库
        String promptTemplate = getPromptTemplate(systemPrompt);
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(new RetrievalRerankAdvisor(vectorStore, rerankModel,
                        searchRequest, new SystemPromptTemplate(promptTemplate), 0.1))
                .build();
        return chatClient.prompt()
                .user(message)
                .stream()
                .chatResponse();
    }

    //判断索引库是否存在
    private void createIndexIfNotExists() {
        try {
            //获取索引库名称
            String indexName = options.getIndexName();
            //获取向量维度
            int dimsLength = options.getDimensions();
            //判断
            if (StringUtils.isBlank(indexName)) {
                throw new RuntimeException("索引库名称不能为空");
            }

            boolean exists = elasticsearchClient.indices().exists(idx->idx.index(indexName)).value();
            if (exists) {
                return;
            }
            //创建索引库
            String similartyAlgo = options.getSimilarity().name();
            IndexSettings indexSettings = IndexSettings.of(settings -> settings.numberOfShards(
                    String.valueOf(1)).numberOfReplicas(String.valueOf(1))
            );
            //创建map集合添加es的属性
            Map<String, Property> properties = new HashMap<>();
            properties.put(vectorField, Property.of(property -> property.denseVector(
                    DenseVectorProperty.of(dense->dense.index( true).dims(dimsLength)
                            .similarity(similartyAlgo))
            )));
            properties.put(textField, Property.of(property -> property.text(TextProperty.of(t->t))));
            //元数据集合
            Map<String, Property> metadata = new HashMap<String, Property>();
            metadata.put("ref_doc_id", Property.of(property -> property.keyword(KeywordProperty.of(k -> k))));

            properties.put("metadata",
                    Property.of(property -> property.object(ObjectProperty.of(op -> op.properties(metadata)))));

            CreateIndexResponse indexResponse = elasticsearchClient.indices()
                    .create(createIndexBuilder -> createIndexBuilder.index(indexName)
                            .settings(indexSettings)
                            .mappings(TypeMapping.of(mappings -> mappings.properties(properties))));
            if (!indexResponse.acknowledged()){
                throw new RuntimeException("创建索引失败");
            }
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String getPromptTemplate(Resource systemResource) {
        try {
            return systemResource.getContentAsString(StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

















