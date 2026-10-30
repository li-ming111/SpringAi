package com.zmx;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.client.autoconfigure.SseHttpClientTransportAutoConfiguration;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = SseHttpClientTransportAutoConfiguration.class)
public class SseApplication {
    public static void main(String[] args) {
        SpringApplication.run(SseApplication.class, args);
    }

    //直接给定问题
    private String userInput1 = "北京的天气如何？";
    private String userInput2 = "将user转为大写";

    @Bean
    public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder,
                                                 ToolCallbackProvider tools,
                                                 ConfigurableApplicationContext context) {
        return args -> {
            var chatClient = chatClientBuilder
                    .defaultToolCallbacks( tools)
                    .build();
            System.out.println("\n>>> 提问: " + userInput1);
            System.out.println("\n>>> 大模型助手: " + chatClient.prompt(userInput1).call().content());
            System.out.println("\n>>> 提问: " + userInput2);
            System.out.println("\n>>> 大模型助手: " + chatClient.prompt(userInput2).call().content());
            context.close();
        };
    }
}
