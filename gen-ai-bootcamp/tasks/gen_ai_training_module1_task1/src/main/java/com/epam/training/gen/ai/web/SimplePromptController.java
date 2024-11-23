package com.epam.training.gen.ai.web;

import com.epam.training.gen.ai.dto.ChatPromptDto;
import com.epam.training.gen.ai.dto.ChatResponseDto;
import com.epam.training.gen.ai.service.SimplePromptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class SimplePromptController {

    private final SimplePromptService service;

    @PostMapping("/prompts")
    public ResponseEntity<ChatResponseDto> promptToChat(@Valid @RequestBody ChatPromptDto request) {
        final var chatResponseDto = new ChatResponseDto(service.getChatCompletions(request.input()));

        return ResponseEntity.ok(chatResponseDto);
    }
}
