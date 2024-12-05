package com.epam.training.gen.ai.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class DeploymentDto {

    private String id;

    private String model;
}
