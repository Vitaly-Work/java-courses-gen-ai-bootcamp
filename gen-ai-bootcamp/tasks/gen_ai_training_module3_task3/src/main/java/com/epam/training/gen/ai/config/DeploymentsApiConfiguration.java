package com.epam.training.gen.ai.config;

import com.epam.training.gen.ai.service.DeploymentsApiClient;

import feign.Feign;
import feign.RequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeploymentsApiConfiguration {
    private static final String HEADER_NAME_API_KEY = "Api-Key";

    private final ApplicationProperties applicationProperties;

    @Bean
    public DeploymentsApiClient deploymentsApiClient() {
        return Feign.builder()
            .requestInterceptor(getDeploymentsApiRequestInterceptor())
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(DeploymentsApiClient.class, applicationProperties.getClientAzureOpenAiEndPoint());
    }

    private RequestInterceptor getDeploymentsApiRequestInterceptor() {
        return template -> template.header(
            HEADER_NAME_API_KEY,
            applicationProperties.getClientAzureOpenAiKey()
        );
    }
}
