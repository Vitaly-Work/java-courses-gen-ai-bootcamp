package com.epam.training.gen.ai.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

import com.epam.training.gen.ai.web.dto.EmbeddingsSearchResultDto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SimpleDocumentService {

    private final SimpleVectorService simpleVectorService;

    @SneakyThrows
    public void savePdfDocument(MultipartFile pdfFile) {
        final var text = getPdfText(pdfFile);
        simpleVectorService.processAndSaveText(text);
    }

    public List<EmbeddingsSearchResultDto> searchContext(String query) {
        return simpleVectorService.search(query);
    }

    @SneakyThrows
    private String getPdfText(MultipartFile pdfFile) {
        final var path = saveToTempDirectory(pdfFile);
        try (PDDocument document = Loader.loadPDF(path.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private Path saveToTempDirectory(MultipartFile file) throws IOException {
        var tempDir = Files.createTempDirectory("uploaded_files_");
        var tempFile = tempDir.resolve(Objects.requireNonNull(file.getOriginalFilename()));

        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        return tempFile;
    }

}
