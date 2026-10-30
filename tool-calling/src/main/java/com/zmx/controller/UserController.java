package com.zmx.controller;

import com.zmx.component.UserInfoTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    private final ChatClient chatClient;

    public UserController(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

     @GetMapping("/userInfo")
    public String getUser() {
        return chatClient.prompt()
                .user("获取用户名")
                .tools(new UserInfoTools())
                .toolContext(Map.of("userId", "123"))
                .call()
                .content();
    }
}
