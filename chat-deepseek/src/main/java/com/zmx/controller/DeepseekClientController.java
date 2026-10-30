package com.zmx.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/deepseek")
public class DeepseekClientController {

    private final ChatClient chatClient;

   /* public DeepseekClientController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }*/

    //构造方法注入
    public DeepseekClientController(DeepSeekChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor
                        .builder(MessageWindowChatMemory.builder().build()).build())
                .defaultOptions(DeepSeekChatOptions.builder().temperature(0.7).build()).build();
    }

    //返回聊天对象
    @GetMapping("/chat")
    public ChatResponse deepseekChat(String message) {
        return chatClient.prompt(new Prompt(
                        message,
                DeepSeekChatOptions.builder().temperature(0.7).build()))
                .call()
                .chatResponse();
    }

    //返回字符串格式
    @GetMapping("/chatString")
    public String deepseekChatString(String message) {
        return chatClient.prompt(message)
                .call()
                .content();
    }

    //流式返回聊天对象
    @GetMapping("/chatStream")
    public Flux<String> deepseekChatStream(String message) {
        return chatClient.prompt(message)
                .stream()
                .content();
    }
}

























