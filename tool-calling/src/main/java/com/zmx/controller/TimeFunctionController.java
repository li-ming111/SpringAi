package com.zmx.controller;

import com.zmx.component.TimeFunction;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeFunctionController {

    private final ChatClient dashScopeChatClient;

    public TimeFunctionController(ChatModel chatModel) {
        this.dashScopeChatClient = ChatClient.builder(chatModel).build();
    }

    //获取当前时间
    @GetMapping("/time")
    public String getTime() {
        return dashScopeChatClient.prompt("获取当前时间")
                .toolNames("getTimeByZoneId")
                .call()
                .content ();
    }

    @GetMapping("/timeTool")
    public String getTimeByZoneId() {
        return dashScopeChatClient.prompt("获取当前时间")
                .toolCallbacks(FunctionToolCallback
                        .builder("getTimeByZoneId",new TimeFunction())
                        .description("按照时区ID获取当前时间")
                        .inputType(TimeFunction.Resquest.class)
                        .build())
                .call()
                .content();
    }
}





























