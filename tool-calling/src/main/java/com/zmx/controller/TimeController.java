/*
package com.zmx.controller;

import com.zmx.component.TimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeController {

    private final ChatClient dashScopeChatClient;
    private final TimeTools timeTools;

    public TimeController(ChatModel chatModel, TimeTools timeTools) {
        this.dashScopeChatClient = ChatClient.builder(chatModel).build();
        this.timeTools = timeTools;
    }

    //不使用工具
    @GetMapping("/time")
    public String getTime(@RequestParam(value = "query", defaultValue = "请告诉我现在杭州时间")
                              String query) {
        return dashScopeChatClient
                .prompt(query)
                .call()
                .content();
    }

    //使用工具,直接创建工具对象
    @GetMapping("/timeTool")
    public String getTimeTool(@RequestParam(value = "query", defaultValue = "请告诉我现在杭州时间")
                          String query) {
        return dashScopeChatClient
                .prompt(query)
                .tools(new TimeTools())
                .call()
                .content();
    }
    //使用工具，注入 工具对象
    @GetMapping("/timeToolAuto")
    public String timeToolAuto(@RequestParam(value = "query", defaultValue = "请告诉我现在上海时间")
                          String query) {
        return dashScopeChatClient
                .prompt(query)
                .tools(timeTools)
                .call()
                .content();
    }
}


























*/
