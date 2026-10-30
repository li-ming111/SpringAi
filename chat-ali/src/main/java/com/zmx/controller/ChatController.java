package com.zmx.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
public class ChatController {

    //聊天对象
    private final ChatClient chatClient;

    //内存记忆
    private final InMemoryChatMemoryRepository memoryRepository = new InMemoryChatMemoryRepository();
    private final int MAX_MEMORY_SIZE = 100;
    //信息窗口
    private final MessageWindowChatMemory messageWindow = MessageWindowChatMemory.builder()
            .chatMemoryRepository(memoryRepository)
            .maxMessages(MAX_MEMORY_SIZE)
            .build();


    //构造方法注入
    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.defaultAdvisors(
                MessageChatMemoryAdvisor.builder(messageWindow)
                        .build()
        ).build();
    }

    //记忆
    @GetMapping("/memory")
    public String memory(String prompt,String userId) {
        return chatClient.prompt(prompt)
                .advisors(p -> p.param(CONVERSATION_ID, userId))
                .call()//交互大模型
                .content();//大模型返回的信息
    }

    @GetMapping("/chat")
    public String chat(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    //流式
    @GetMapping("/chat/stream")
    public Flux<String> chatStream(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .stream()
                .content();
    }
    @GetMapping("/chat/stream1")
    public Flux<String> chatStream1(String prompt, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return chatClient.prompt(prompt)
                .stream()
                .content();
    }


}



























