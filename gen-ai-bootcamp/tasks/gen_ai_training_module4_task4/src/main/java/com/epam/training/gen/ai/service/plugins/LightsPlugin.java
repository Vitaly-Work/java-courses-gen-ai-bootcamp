package com.epam.training.gen.ai.service.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LightsPlugin {

    // Mock data for the lights
    private final Map<Integer, LightModel> lights = new HashMap<>();

    public LightsPlugin() {
        lights.put(1, new LightModel(1, "Table Lamp", false));
        lights.put(2, new LightModel(2, "Porch light", false));
        lights.put(3, new LightModel(3, "Chandelier", true));
    }

    @DefineKernelFunction(name = "get_lights", description = "Gets a list of lights and their current state. "
        + "Returned result is a list of structures (models). Example of model:  " +
        "{\"id\":99,\"name\":\"Head Lamp\",\"isOn\":false,\"brightness\":\"MEDIUM\",\"color\":\"#FFFFFF\"} "
        + " The state of light coded in model in field isOn as a boolean (true - light is on, false - light is off)",
        returnType = "com.epam.training.gen.ai.service.plugins.LightsList")
    public LightsList getLights() {
        final var lightsList = new LightsList(new ArrayList<>(lights.values()));
        log.info("Function 'get_lights' called with result: {}", lightsList);

        return lightsList;
    }


    @DefineKernelFunction(name = "change_state_of_light_by_id", description = "Changes the state of the light. "
        + "Requires 2 parameters: the ID of light as number and the new state of light as boolean. Returns null if light was not found by ID and you should get the ID of light again",
        returnType = "com.epam.training.gen.ai.service.plugins.LightModel")
    public LightModel changeState(
        @KernelFunctionParameter(name = "id", description = "The ID of the light to change as number. You should pass here a digit, without quotes.") int id,
        @KernelFunctionParameter(name = "isOn", description = "The new state of the light as boolean. You should pass here only true or false, without quotes.") boolean isOn) {
        log.info("Function 'change_state_of_light_by_id' called with arguments: {}, {}", id, isOn);
        if (!lights.containsKey(id)) {
            return null;
        }

        lights.get(id).setOn(isOn);

        return lights.get(id);
    }

}
