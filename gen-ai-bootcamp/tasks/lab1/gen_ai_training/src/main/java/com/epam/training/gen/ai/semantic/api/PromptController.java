package com.epam.training.gen.ai.semantic.api;

import com.epam.training.gen.ai.PromptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/generate")
@Slf4j
public class PromptController {

    @Autowired
    private final PromptService promptService;

    public PromptController(PromptService promptService) {
        this.promptService = promptService;
    }

    @GetMapping
    public Mono<ResponseEntity<List<String>>> generateResponse(@RequestParam String prompt) {
        log.info("prompt value: {}", prompt);
        return Mono.just(promptService.generateResponse(prompt))
                .map(ResponseEntity::ok);
    }
}
