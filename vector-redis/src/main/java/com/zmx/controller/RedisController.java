package com.zmx.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/redis")
public class RedisController {

    private final RedisVectorStore redisVectorStore;

    @Autowired
    public RedisController(@Qualifier("redisVectorStoreCustom")RedisVectorStore redisVectorStore) {
        this.redisVectorStore = redisVectorStore;
    }

    //添加向量
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
        redisVectorStore.add(documents);
    }
    //检索
    @GetMapping("/search")
    public List<Document> search() {
        return redisVectorStore.similaritySearch(
                SearchRequest
                .builder()
                .query("美好")
                .topK(2)
                .build());
    }
    //删除
    @GetMapping("/delete")
    public void delete() {
       //查询的对象
        FilterExpressionBuilder builder = new FilterExpressionBuilder();
        Filter.Expression expression = builder.eq("name", "张三").build();
        //按照过滤条件删除向量
        redisVectorStore.delete(expression);
    }
}

































