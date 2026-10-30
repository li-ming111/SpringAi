package com.zmx.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.zmx.domain.ActorFilms;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController1 {

    private final ChatClient chatClient;
    //构造方法注入
    public ChatController1(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/chat1")
    public String chat(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    @GetMapping("/chat2")
    public ActorFilms chat2(String prompt) {
        ActorFilms actorFilms = chatClient.prompt()
                .user(prompt)
                .call()
                .entity(ActorFilms.class);
        return actorFilms;
    }

    @GetMapping("/chat3")
    public ChatResponse chat3(String prompt) {
        //大模型响应的类型
        ChatResponse chatResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .chatResponse();
        return chatResponse;
    }
}



























