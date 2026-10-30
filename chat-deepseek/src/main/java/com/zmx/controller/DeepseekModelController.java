package com.zmx.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class DeepseekModelController {

    private final ChatModel chatModel;
    //构造方法注入
    public DeepseekModelController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    //字符串格式
    @GetMapping("/chatStringModel")
    public String chatStringModel(String message) {
        return chatModel.call(new Prompt(message))
                .getResult()
                .getOutput()
                .getText();
    }

    //字符串格式：流式输入
    @GetMapping("/chatStringModelStream")
    public Flux<String> chatStringModelStream(String message, HttpServletResponse  response) {
        response.setCharacterEncoding("UTF-8");
        Flux<ChatResponse> stream = chatModel.stream(new Prompt(message));
        return stream.map(chatResponse -> chatResponse.getResult().getOutput().getText());
    }
}






























