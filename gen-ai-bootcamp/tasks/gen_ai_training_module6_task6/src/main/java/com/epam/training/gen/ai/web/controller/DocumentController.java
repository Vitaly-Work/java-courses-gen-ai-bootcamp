package com.epam.training.gen.ai.web.controller;

import com.epam.training.gen.ai.service.SimpleDocumentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v2/document")
@RequiredArgsConstructor
public class DocumentController {
    private final SimpleDocumentService simpleDocumentService;

    @PostMapping("/upload-pdf")
    public ResponseEntity<Void> uploadPdf(@RequestParam MultipartFile pdfFile) {
        simpleDocumentService.savePdfDocument(pdfFile);
        return ResponseEntity.ok().build();
    }

}

