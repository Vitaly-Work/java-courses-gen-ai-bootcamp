package com.epam.training.gen.ai.config;

import java.util.Map;

import com.epam.training.gen.ai.service.PromptWithHistoryService;
import com.epam.training.gen.ai.service.SimplePromptService;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for main configurable application components.
 * <p>
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    private final ApplicationProperties applicationProperties;

    /**
     * Creates an {@link SimplePromptService} bean of service class for
     * generating chat completions using Azure OpenAI.
     *
     * @return an instance of {@link SimplePromptService}
     */
    @Bean
    public SimplePromptService simplePromptService(OpenAIAsyncClient openAIAsyncClient) {
        return SimplePromptService.builder()
            .applicationProperties(applicationProperties)
            .aiAsyncClient(openAIAsyncClient)
            .build();
    }

    /**
     * Creates an {@link PromptWithHistoryService} bean of service class for
     * generating chat completions using Azure OpenAI.
     *
     * @return an instance of {@link PromptWithHistoryService}
     */
    @Bean
    public PromptWithHistoryService promptWithHistoryService(Kernel kernel,
                                                             Map<String, PromptExecutionSettings> promptExecutionSettings) {
        return PromptWithHistoryService.builder()
            .kernel(kernel)
            .promptExecutionSettings(promptExecutionSettings)
            .build();
    }
}
