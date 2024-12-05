package com.epam.training.gen.ai.web.controller;

import java.util.List;

import com.epam.training.gen.ai.service.DeploymentsService;
import com.epam.training.gen.ai.web.dto.DeploymentDto;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/deployments")
@RequiredArgsConstructor
public class DeploymentsController {

    private final DeploymentsService deploymentsService;

    @GetMapping
    public List<DeploymentDto> getDeployments() {
        return deploymentsService.getDeployments();
    }

    @GetMapping("/update")
    public void refreshDeployments() {
        deploymentsService.updateDeployments();
    }

    @GetMapping("/switch")
    public void switchDeployment(@RequestParam String deploymentName) {
        deploymentsService.switchDeployment(deploymentName);
    }

}
