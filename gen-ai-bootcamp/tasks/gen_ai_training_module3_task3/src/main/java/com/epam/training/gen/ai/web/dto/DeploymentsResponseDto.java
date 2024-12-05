package com.epam.training.gen.ai.web.dto;

import java.util.List;

import lombok.Data;

@Data
public class DeploymentsResponseDto {

    private List<DeploymentDto> data;
}
