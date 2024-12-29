package com.epam.training.gen.ai.web.controller;

import com.epam.training.gen.ai.service.PromptWithHistoryService;
import com.epam.training.gen.ai.web.dto.ChatPromptDto;
import com.epam.training.gen.ai.web.dto.ChatResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/chat")
@RequiredArgsConstructor
public class PromptWithHistoryController {

    private final PromptWithHistoryService service;

    @PostMapping("/prompts")
    public ResponseEntity<ChatResponseDto> promptToChat(@Valid @RequestBody ChatPromptDto request) {
        final var chatResponseDto = new ChatResponseDto(service.chatWithHistory(request.input()));

        return ResponseEntity.ok(chatResponseDto);
    }
}
