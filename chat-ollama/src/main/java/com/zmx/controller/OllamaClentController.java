package com.zmx.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ollama")
public class OllamaClentController {

    private final ChatClient chatClient;
    //构造方法注入
    public OllamaClentController(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultOptions(
                        OllamaOptions.builder()
                                .topP(0.7)
                                .model("qwen3:8b")
                                .build()
                ).build();
    }

    @GetMapping("/chat")
    public String chat(String message) {
        //return chatClient.prompt(message).call().content();
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/chatStream")
    public Flux<String> chatStream(String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

}
