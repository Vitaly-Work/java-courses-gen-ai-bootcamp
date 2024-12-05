package com.epam.training.gen.ai.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to contain application properties from config.
 * <p>
 */
@Configuration
@Getter
public class ApplicationProperties {

    @Value("${client-azureopenai-key}")
    private String clientAzureOpenAiKey;
    @Value("${client-azureopenai-endpoint}")
    private String clientAzureOpenAiEndPoint;

    @Value("${client-azureopenai-deployment-name}")
    private String deploymentOrModelName;
    @Value("${chat-azureopenai-temperature}")
    private Double temperature;

}
