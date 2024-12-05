package com.epam.training.gen.ai.config;

import java.util.Map;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Semantic Kernel components.
 * <p>
 * This configuration provides several beans necessary for the interaction with
 * Azure OpenAI services and the creation of kernel plugins. It defines beans for
 * chat completion services, kernel plugins, kernel instance, invocation context,
 * and prompt execution settings.
 */
@Configuration
@RequiredArgsConstructor
public class SemanticKernelConfiguration {

    private final ApplicationProperties applicationProperties;

    /**
     * Creates a {@link ChatCompletionService} bean for handling chat completions using Azure OpenAI.
     *
     * @param openAIAsyncClient the {@link OpenAIAsyncClient} to communicate with Azure OpenAI
     * @return an instance of {@link ChatCompletionService}
     */
    @Bean
    public ChatCompletionService chatCompletionService(OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
            .withModelId(applicationProperties.getDeploymentOrModelName())
            .withOpenAIAsyncClient(openAIAsyncClient)
            .build();
    }

    /**
     * Creates a {@link Kernel} bean to manage AI services and plugins.
     *
     * @param chatCompletionService the {@link ChatCompletionService} for handling completions
     * @return an instance of {@link Kernel}
     */
    @Bean
    public Kernel kernel(ChatCompletionService chatCompletionService) {
        return Kernel.builder()
            .withAIService(ChatCompletionService.class, chatCompletionService)
            .build();
    }

    /**
     * Creates an {@link InvocationContext} bean with default prompt execution settings.
     *
     * @return an instance of {@link InvocationContext}
     */
    @Bean
    public InvocationContext invocationContext() {
        return InvocationContext.builder()
            .withPromptExecutionSettings(PromptExecutionSettings.builder()
                .withTemperature(1.0)
                .build())
            .build();
    }

    /**
     * Creates a map of {@link PromptExecutionSettings} for different models.
     *
     * @return a map of model names to {@link PromptExecutionSettings}
     */
    @Bean
    public Map<String, PromptExecutionSettings> promptExecutionsSettingsMap() {
        return Map.of(
            applicationProperties.getDeploymentOrModelName(), PromptExecutionSettings.builder()
                .withTemperature(applicationProperties.getTemperature())
                .build()
        );
    }
}

