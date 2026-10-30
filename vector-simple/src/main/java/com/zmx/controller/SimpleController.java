package com.zmx.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/simple")
public class SimpleController {

    private final SimpleVectorStore simpleVectorStore;
    private final String SAVE_PATH = "d:/zmx/simpleSave.json";

    public SimpleController(EmbeddingModel embeddingModel) {
        this.simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
    }

    // 添加向量
    @GetMapping("/add")
    public void add() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("year", 2025);
        map.put("id","1001");

        //集合
        List<Document> documents = List.of(
                new Document("这世界很美好"),
                new Document("勇敢的面对你的未来", Map.of("year",2026)),
                new Document("中国强大了，富裕了，美好了", map)
        );

        //添加到缓存中
        simpleVectorStore.add(documents);
    }

    @GetMapping("/save")
    public void save()  {
        File file = new File(SAVE_PATH);
        if (file.exists()) {
            file.delete();
        }
        //把上面方法执行后的数据保存到文件中
        simpleVectorStore.save(file);
    }

    //按照id删除向量
    @GetMapping("/delete")
    public void delete() {
        simpleVectorStore.delete(List.of("1001"));
    }

    //加载文件到simpleVectorStore
    @GetMapping("/load")
    public void load() {
        File file = new File(SAVE_PATH);
        if (file.exists()) {
            simpleVectorStore.load(file);
        }
    }

    //查询向量
    @GetMapping("/search")
    public List<Document> search() {
        return simpleVectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("美好")
                        .topK(5)
                        .build());
    }

    //表达式过滤
    @GetMapping("/filter")
    public List<Document> filter() {
        FilterExpressionBuilder builder = new FilterExpressionBuilder();
        Filter.Expression expression = builder.and(builder.in("year", 2025),
                builder.eq("name", "张三")).build();

        return simpleVectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("美好")
                        .topK(5)
                        .filterExpression(expression)
                        .build()
        );
    }
}















































