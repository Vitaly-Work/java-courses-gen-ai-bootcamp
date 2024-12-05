package com.epam.training.gen.ai.config;

import java.util.Map;

import lombok.Data;

@Data
public class DeploymentsSettings {
    public static final String DEFAULT_DEPLOYMENT_SETTINGS_KEY = "default-settings";

    private Map<String, DeploymentSettings> deploymentSettings;
}
