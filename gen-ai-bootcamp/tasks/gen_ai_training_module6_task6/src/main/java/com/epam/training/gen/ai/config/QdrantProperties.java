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
public class QdrantProperties {

    @Value("${qdrant-host}")
    private String host;
    @Value("${qdrant-port}")
    private int port;
    @Value("${qdrant-collection-name}")
    private String collectionName;
    @Value("${qdrant-search-result-limit}")
    private int searchResultLimit;

}
