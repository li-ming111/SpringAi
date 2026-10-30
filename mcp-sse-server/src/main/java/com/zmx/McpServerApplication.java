package com.zmx;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpServerApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(McpServerApplication.class, args);
    }

    //注入天气组件
    @Bean
    public ToolCallbackProvider weatherTools(OpenMeteoService openMeteoService) {
        return MethodToolCallbackProvider.builder().toolObjects(openMeteoService).build() ;
    }

    //输入
    public record TextInput(String input) {
    }

    //大小写转换
    @Bean
    public ToolCallback toUpperCase() {
        return FunctionToolCallback.builder("toUpperCase",
                (TextInput input)->input.input().toUpperCase())
                .inputType(TextInput.class)
                .description("将输入的字符串转换为大写")
                .build();
    }
}
























