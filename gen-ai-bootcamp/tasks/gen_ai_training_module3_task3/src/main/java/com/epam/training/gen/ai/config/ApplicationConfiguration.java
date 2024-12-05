package com.epam.training.gen.ai.config;

import com.epam.training.gen.ai.service.DeploymentsApiClient;
import com.epam.training.gen.ai.service.DeploymentsService;
import com.epam.training.gen.ai.service.PromptWithHistoryService;

import com.microsoft.semantickernel.Kernel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for main configurable application components.
 * <p>
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

    /**
     * Creates an {@link PromptWithHistoryService} bean of service class for
     * generating chat completions using Azure OpenAI.
     *
     * @return an instance of {@link PromptWithHistoryService}
     */
    @Bean
    public PromptWithHistoryService promptWithHistoryService(ApplicationProperties applicationProperties,
                                                             Kernel kernel,
                                                             DeploymentsSettings deploymentsSettings) {
        return PromptWithHistoryService.builder()
            .applicationProperties(applicationProperties)
            .kernel(kernel)
            .deploymentsSettings(deploymentsSettings)
            .build();
    }

    /**
     * Creates an {@link DeploymentsService} bean of service class for
     * manipulations with different models.
     *
     * @return an instance of {@link DeploymentsService}
     */
    @Bean
    public DeploymentsService deploymentsService(ApplicationProperties applicationProperties,
                                                 DeploymentsApiClient deploymentsApiClient,
                                                 PromptWithHistoryService promptWithHistoryService) {
        final var deployments = deploymentsApiClient.getDeployments().getData();
        log.info("Loaded deployments: {}", deployments);

        return DeploymentsService.builder()
            .applicationProperties(applicationProperties)
            .deploymentsApiClient(deploymentsApiClient)
            .promptWithHistoryService(promptWithHistoryService)
            .deployments(deployments)
            .build();
    }

}
