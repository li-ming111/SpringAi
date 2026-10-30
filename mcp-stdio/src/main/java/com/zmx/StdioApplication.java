package com.zmx;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StdioApplication {
    public static void main(String[] args) {
        SpringApplication.run(StdioApplication.class, args);
    }

    //用户输入
    private String userInput="杭州的天气如何？";

    @Bean
    public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder,
                                                 ToolCallbackProvider tools,
                                                 ConfigurableApplicationContext context) {

        return args -> {
            var chatClient = chatClientBuilder
                    .defaultToolCallbacks(tools)
                    .build();

            System.out.println("\n>>> 提问: " + userInput);
            System.out.println("\n>>> 大模型助手: " + chatClient.prompt(userInput).call().content());

            context.close();
        };
    }
}






































