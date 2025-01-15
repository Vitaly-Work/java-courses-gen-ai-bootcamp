package com.epam.training.gen.ai.web.controller;

import java.util.List;

import com.epam.training.gen.ai.service.SimpleVectorService;
import com.epam.training.gen.ai.web.dto.EmbeddingsRequestDto;
import com.epam.training.gen.ai.web.dto.EmbeddingsSearchResultDto;

import com.azure.ai.openai.models.EmbeddingItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/embedding")
@RequiredArgsConstructor
public class EmbeddingsController {

    private final SimpleVectorService simpleVectorService;


    @PostMapping("/build")
    public ResponseEntity<List<EmbeddingItem>> build(@RequestBody EmbeddingsRequestDto request) {
        final var embeddings = simpleVectorService.getEmbeddings(request.text());

        return ResponseEntity.ok(embeddings);
    }

    @PostMapping("/build-and-store")
    public ResponseEntity<Void> buildAndStore(@RequestBody EmbeddingsRequestDto request) {

        simpleVectorService.processAndSaveText(request.text());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/search-closest")
    public ResponseEntity<List<EmbeddingsSearchResultDto>> searchClosest(
        @RequestBody EmbeddingsRequestDto request) {

        final var results = simpleVectorService.search(request.text());

        return ResponseEntity.ok(results);
    }
}

