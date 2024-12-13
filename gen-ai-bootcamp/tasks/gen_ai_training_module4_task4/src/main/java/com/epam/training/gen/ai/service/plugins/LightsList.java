package com.epam.training.gen.ai.service.plugins;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LightsList {
    private List<LightModel> lights;
}
