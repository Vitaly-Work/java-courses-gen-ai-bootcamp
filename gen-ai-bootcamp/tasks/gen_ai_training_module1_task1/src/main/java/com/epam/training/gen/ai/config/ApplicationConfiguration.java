package com.epam.training.gen.ai.config;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.service.SimplePromptService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for main configurable application components.
 * <p>
 */
@Configuration
public class ApplicationConfiguration {

    @Value("${client-azureopenai-deployment-name}")
    private String deploymentOrModelName;
    @Value("${chat-azureopenai-temperature}")
    private Double samplingTemperature;

    /**
     * Creates an {@link SimplePromptService} bean of service class for
     * generating chat completions using Azure OpenAI.
     *
     * @return an instance of {@link SimplePromptService}
     */
    @Bean
    public SimplePromptService simplePromptService(OpenAIAsyncClient openAIAsyncClient) {
        return SimplePromptService.builder()
                .aiAsyncClient(openAIAsyncClient)
                .deploymentOrModelName(deploymentOrModelName)
                .samplingTemperature(samplingTemperature)
                .build();
    }
}
