package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.web.dto.DeploymentsResponseDto;

import feign.Headers;
import feign.RequestLine;

@Headers("Content-Type: application/json")
public interface DeploymentsApiClient {

    @RequestLine("GET /openai/deployments")
    DeploymentsResponseDto getDeployments();
}
