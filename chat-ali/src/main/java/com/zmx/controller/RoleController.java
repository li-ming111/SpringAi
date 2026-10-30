package com.zmx.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
public class RoleController {

    private final ChatClient chatClient;

    //构造函数
    public RoleController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    };

    //系统提示词
    @Value("classpath:/message/system-message.txt")
    private Resource systemResource;

    @GetMapping("/role")
    public Flux <String> role(
            @RequestParam (required = false, defaultValue = "张三") String name,
            @RequestParam(required = false, defaultValue = "高层经理")String voice,
    String message) {
        //创建用户消息对象
        UserMessage userMessage = new UserMessage(message);
        //使用提示词模板
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemResource);
        //替换提示词模板中的变量
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", name, "voice", voice));
        //调用大模型进行处理
        return chatClient.prompt(new Prompt(List.of(userMessage,systemMessage)))
                .stream()
                .content();
    }
}












































