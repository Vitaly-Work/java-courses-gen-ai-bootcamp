package com.epam.training.gen.ai.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatPromptDto(@NotBlank(message = "Prompt input is required") String input) {
}
