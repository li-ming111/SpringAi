package com.zmx.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DocumentController {

    @GetMapping("/document")
    public List<Document> document() {
        //获取模型API
        var dashScopeApi = DashScopeApi.builder().apiKey(System.getenv("DASHSCOPE_API_KEY")).build();
        //获取文档列表
        DocumentRetriever retriever = new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder()
                        .withIndexName("spring").build());//索引名称
        //获取文档列表
        Query query = new Query("知识库");//查询关键字
        List<Document> list = retriever.retrieve(query);
        return list;
    }
}
