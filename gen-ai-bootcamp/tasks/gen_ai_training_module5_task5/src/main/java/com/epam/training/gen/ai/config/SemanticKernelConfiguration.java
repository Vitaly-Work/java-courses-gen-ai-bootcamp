package com.epam.training.gen.ai.config;

import static com.epam.training.gen.ai.config.DeploymentsSettings.DEFAULT_DEPLOYMENT_SETTINGS_KEY;

import java.io.InputStream;
import java.util.Optional;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

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
    public InvocationContext invocationContext(PromptExecutionSettings promptExecutionSettings) {
        return new InvocationContext.Builder()
            .withReturnMode(InvocationReturnMode.NEW_MESSAGES_ONLY)
            .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
            .withPromptExecutionSettings(promptExecutionSettings)
            .build();
    }


    /**
     * Returns the {@link PromptExecutionSettings} related to current deployment.
     *
     * @return a {@link PromptExecutionSettings}
     */
    @Bean
    public PromptExecutionSettings promptExecutionSettings(DeploymentsSettings deploymentsSettings) {
        final var name = applicationProperties.getDeploymentOrModelName();
        final var settingsMap = deploymentsSettings.getDeploymentSettings();

        return Optional.ofNullable(settingsMap.get(name))

            .map(settings -> PromptExecutionSettings.builder()
                .withModelId(name)
                .withTemperature(settings.temperature())
                .withMaxTokens(settings.maxTokens())
                .build())

            .orElseGet(() -> {
                final var settings = deploymentsSettings.getDeploymentSettings().get(DEFAULT_DEPLOYMENT_SETTINGS_KEY);
                return PromptExecutionSettings.builder()
                    .withModelId(name)
                    .withTemperature(settings.temperature())
                    .withMaxTokens(settings.maxTokens())
                    .build();
            });
    }


    /**
     * Creates an {@link DeploymentsSettings} bean for
     * storing settings for different deployments.
     *
     * @return an instance of {@link DeploymentsSettings}
     */
    @SneakyThrows
    @Bean
    public DeploymentsSettings deploymentsSettings() {
        InputStream inputStream = new ClassPathResource("config/deployments-settings.json").getInputStream();

        final var deploymentsSettings = new ObjectMapper().readValue(inputStream, DeploymentsSettings.class);
        deploymentsSettings.getDeploymentSettings().put(
            DEFAULT_DEPLOYMENT_SETTINGS_KEY,
            new DeploymentSettings(applicationProperties.getTemperature(), PromptExecutionSettings.DEFAULT_MAX_TOKENS)
        );
        return deploymentsSettings;
    }

}

