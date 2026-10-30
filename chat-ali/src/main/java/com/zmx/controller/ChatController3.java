package com.zmx.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController3 {

    private final ChatModel chatModel;
    //构造方法注入
    public ChatController3(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    //编码设置模型
    @GetMapping("/chatModel")
    public String chatModel(String prompt) {
       return chatModel.call(new Prompt(prompt,DashScopeChatOptions
               .builder()
               .withModel(DashScopeApi.ChatModel.QWEN_PLUS.getModel())//优先级高于配置文件
               .build()))
               .getResult()
               .getOutput()
               .getText();
    }

    //流式返回
    @GetMapping("/chatStream")
    public Flux<String> chatStream(HttpServletResponse response, String prompt) {
        //设置编码
        response.setCharacterEncoding("UTF-8");
        Flux<ChatResponse> stream = chatModel.stream(new Prompt(prompt, DashScopeChatOptions
                .builder()
                .withModel(DashScopeApi.ChatModel.QWEN_PLUS.getModel())
                .build()));
        return stream.map(resp -> resp.getResult().getOutput().getText());
    }

    @GetMapping("/chatStream1")
    public Flux<String> chatStream1(HttpServletResponse response, String prompt) {
        //设置编码
        response.setCharacterEncoding("UTF-8");
        DashScopeChatOptions chatOptions = DashScopeChatOptions.builder()
                .withTopP(0.7)// 温度
                .withTopK(10) // 采样
                .withTemperature(0.8) //
                .withMaxToken(100) // 最大token数
                .build();

        return chatModel.stream(new Prompt(prompt, chatOptions))
                .map(resp -> resp.getResult().getOutput().getText());
    }
}



























