package com.epam.training.gen.ai;

import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.epam.training.gen.ai.semantic.client.OpenAIAsyncClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PromptService {
    private final OpenAIAsyncClientService aiAsyncClientService;
    private final String deploymentOrModelName;


    public PromptService(OpenAIAsyncClientService aiAsyncClientService,
                         @Value("${client-azureopenai-deployment-name}")  String deploymentOrModelName) {
        this.aiAsyncClientService = aiAsyncClientService;
        this.deploymentOrModelName = deploymentOrModelName;
    }

    public List<String>generateResponse(String message) {
        ChatCompletions completions = aiAsyncClientService.get()
                .getChatCompletions(
                        deploymentOrModelName,
                        new ChatCompletionsOptions(
                                List.of(new ChatRequestUserMessage(message))))
                .block();
        List<String> messages = completions.getChoices().stream()
                .map(c -> c.getMessage().getContent())
                .collect(Collectors.toList());
        log.info(messages.toString());
        return messages;
    }
}
