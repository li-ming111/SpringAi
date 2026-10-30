package com.zmx.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class OllamaModelController {

    private final ChatModel chatModel;

    public OllamaModelController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    // 返回一个字符串
    @GetMapping("/chatStringModel")
    public String chatStringModel(String message) {
        String text = chatModel.call(new Prompt(message))
                .getResult()
                .getOutput()
                .getText();
        return text;
    }

    //流的形式返回
    @GetMapping("/chatStreamModel")
    public Flux<String> chatStreamModel(String message) {
        Flux<ChatResponse> stream = chatModel.stream(new Prompt(message));
        return stream.map(chatResponse -> chatResponse.getResult().getOutput().getText());
    }

    //模型调用:选项
    @GetMapping("/chatOptionModel")
    public String chatOptionModel(String message) {
        //创建选项
        OllamaOptions ollamaOptions = OllamaOptions.builder()
                .topP(0.5)
                .model("qwen2.5:3b")
                .temperature(0.5)
                .build();
        return chatModel.call(new Prompt(message,ollamaOptions)).getResult().getOutput().getText();
    }
}






























