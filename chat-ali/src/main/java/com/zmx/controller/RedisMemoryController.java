package com.zmx.controller;

import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
public class RedisMemoryController {

    private final ChatClient chatClient;

    private final int MAX_MEMORY = 100;
    private final MessageWindowChatMemory messageWindowChatMemory;

    //构造方法注入
    public RedisMemoryController(ChatClient.Builder builder, RedisChatMemoryRepository redisChatMemoryRepository) {
        messageWindowChatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(MAX_MEMORY)
                .build();
        chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(messageWindowChatMemory).build()).build();
    }

    @GetMapping("/redisMemory")
    public String redisMemory(String message,String userId) {
        return chatClient.prompt(message)
                .advisors(a -> a.param(CONVERSATION_ID, userId))
                .call()
                .content();
    }
}
