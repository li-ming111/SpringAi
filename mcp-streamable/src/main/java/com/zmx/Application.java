package com.zmx;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication(exclude = {
        org.springframework.ai.mcp.client.autoconfigure.SseHttpClientTransportAutoConfiguration.class,
})
@ComponentScan(basePackages = "org.springframework.ai.mcp.client")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private String userInput = "上海的天气";

    @Bean
    public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools,
            ConfigurableApplicationContext context) {

        return args -> {

            var chatClient = chatClientBuilder
                    .defaultToolCallbacks(tools)
                    .build();

            System.out.println("\n>>> 提问: " + userInput);
            System.out.println("\n>>> 回答: " + chatClient.prompt(userInput).call().content());

            System.out.println("\n>>> 提问: " + "把hello转大写");
            System.out.println("\n>>> 回答: " + chatClient.prompt("把hello转大写").call().content());
        };
    }
}
