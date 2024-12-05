package com.epam.training.gen.ai.service;

import java.util.List;

import com.epam.training.gen.ai.config.ApplicationProperties;
import com.epam.training.gen.ai.web.dto.DeploymentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Builder
public class DeploymentsService {
    private final ApplicationProperties applicationProperties;
    private final DeploymentsApiClient deploymentsApiClient;
    private final PromptWithHistoryService promptWithHistoryService;

    private volatile List<DeploymentDto> deployments;

    public List<DeploymentDto> getDeployments() {
        return deploymentsApiClient.getDeployments().getData();
    }

    public void updateDeployments() {
        deployments = deploymentsApiClient.getDeployments().getData();
        log.info("Updated deployments: {}", deployments);
    }

    public void switchDeployment(String deploymentName) {
        validate(deploymentName);
        synchronized (this) {
            promptWithHistoryService.clearChatHistory();
            applicationProperties.setDeploymentOrModelName(deploymentName);
        }
    }

    private void validate(String deploymentName) {
        deployments.stream()
            .filter(dto -> deploymentName.equals(dto.getModel()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Deployment not found by name: " + deploymentName + ". Try to update deployments"));
    }

}
