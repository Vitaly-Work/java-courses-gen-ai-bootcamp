package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import com.epam.training.gen.ai.config.ApplicationProperties;

/**
 * Service class for generating chat completions using Azure OpenAI.
 * <p>
 * This service interacts with the Azure OpenAI API to generate chat completions
 * based on a static greeting message. It retrieves responses from the AI model
 * and logs them.
 */
@Slf4j
@RequiredArgsConstructor
@Builder
public class SimplePromptService {

    private final ApplicationProperties applicationProperties;
    private final OpenAIAsyncClient aiAsyncClient;

    public List<String> getChatCompletions(String prompt) {
        log.info("Request>>>>>>>> \n {}", prompt);

        final var chatRequestUserMessage = new ChatRequestUserMessage(prompt);
        final var chatCompletionsOptions = getChatCompletionsOptions(List.of(chatRequestUserMessage));
        final var chatCompletions = aiAsyncClient
                .getChatCompletions(applicationProperties.getDeploymentOrModelName(), chatCompletionsOptions)
                .block();
        final var messages = Optional.ofNullable(chatCompletions)
                .map(completions -> completions.getChoices().stream()
                        .map(c -> c.getMessage().getContent())
                        .toList())
                .orElse(List.of());

        log.info("Response<<<<<<<< \n {}", messages);

        return messages;
    }

    private ChatCompletionsOptions getChatCompletionsOptions(List<ChatRequestMessage> chatRequestMessages) {
        final var chatCompletionsOptions = new ChatCompletionsOptions(chatRequestMessages);
        chatCompletionsOptions.setTemperature(applicationProperties.getTemperature());

        return chatCompletionsOptions;
    }

}


