package com.zmx.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.HashMap;

@RestController
public class StuffController {

    private final ChatClient chatClient;

    //构造函数
    public StuffController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    };

    //读取数据文件
    @Value("classpath:/data/bikes.json")
    private Resource docToStuffResource;

    //读取系统提示词文件
    @Value("classpath:/message/system-qa.txt")
    private Resource qaPromptResource;

    /**
     * 使用特定的prompt和QA对话，增强大模型的回答
     */
    @GetMapping("/stuff")
    public Flux<String> stuff( String message) {
        //提示词
        PromptTemplate promptTemplate = new PromptTemplate(qaPromptResource);
        //创建上下文
        HashMap<String, Object> map = new HashMap<>();
        //添加文件内容
        map.put("question",message);
        map.put("question_answer_context",docToStuffResource);
        //调用大模型进行处理
        return chatClient.prompt(promptTemplate.create(map))
                .stream()
                .content();
    }
}


























