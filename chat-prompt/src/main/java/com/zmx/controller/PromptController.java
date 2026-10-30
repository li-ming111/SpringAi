package com.zmx.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromptController {

    private final ChatClient chatClient;

    public PromptController(ChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }

    @GetMapping("/prompt")
    public String prompt(String message) {
        return chatClient.prompt()
                .system("这是系统提示词")
                .user("用户：" + message)
                .options(ChatOptions.builder()
                        .model("qwen3-30b-a3b")
                        .temperature(0.9)
                        .topP(0.5)
                        .topK(5)
                        .maxTokens(100)
                        .build())
                .call()
                .content();
    }

    //提示词添加参数
    @GetMapping("/stuff")
    public String stuff(String system,String user ) {
        return chatClient.prompt()
                .system(s->s.text("这是系统提示词:{systemContext}")
                        .param("systemContext",system))
                .user(u->u.text("这是用户输入:{userContext}")
                        .param("userContext",user))
                .call()
                .content();
    }
}









































