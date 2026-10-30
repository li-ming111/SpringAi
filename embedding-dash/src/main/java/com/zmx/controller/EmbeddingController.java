package com.zmx.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class EmbeddingController {

    @GetMapping("/ai/embedding")
    public EmbeddingResponse embed( String message) {
        //获取模型API
        var dashScopeApi = DashScopeApi.builder().apiKey(System.getenv("DASHSCOPE_API_KEY")).build();
        //创建模型对象
        var embeddingModel = new DashScopeEmbeddingModel(
                dashScopeApi,
                MetadataMode.EMBED,//嵌入模式
                DashScopeEmbeddingOptions.builder()
                        .withModel("text-embedding-v2")//模型名称
                        .build()
        );
        //调用模型
        EmbeddingResponse embeddingResponse = embeddingModel
                .embedForResponse(List.of("大家好", "苏超足球队"));
        return embeddingResponse;
    }
}








































