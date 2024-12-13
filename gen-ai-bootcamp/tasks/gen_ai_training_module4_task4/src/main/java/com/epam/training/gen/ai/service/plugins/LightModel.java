package com.epam.training.gen.ai.service.plugins;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LightModel {
    private int id;
    private String name;
    private boolean isOn;
}
