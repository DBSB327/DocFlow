package com.neoskat.docflow.extractor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentExtractorFactory {
    private final PdfContentExtractor pdfExtractor;
    private final TextFileContentExtractor textExtractor;
    private final OcrContentExtractor ocrExtractor;

    public ContentExtractor getExtractor(String filePath) {
        if (filePath.endsWith(".pdf")) {
            return pdfExtractor;
        } else if (filePath.endsWith(".png") || filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            return ocrExtractor;
        } else if (filePath.endsWith(".txt")) {
            return textExtractor;
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + filePath);
        }
    }
}

