package com.zmx.service;

import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

public interface RagService {

    //添加向量
    void importDocuments();
    //搜索向量
    Flux<ChatResponse> retrieve(String message);
}
