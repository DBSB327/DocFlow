package com.neoskat.docflow.controller;

import com.neoskat.docflow.model.Document;
import com.neoskat.docflow.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        documentService.uploadAndIndexFile(file);
        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/search")
    public List<String> searchFiles(@RequestParam("query") String query) throws Exception {
        return documentService.searchDocuments(query);
    }

    @GetMapping("/searchByName/{fileName}")
    public ResponseEntity<List<Document>> getDocumentMetadata(@PathVariable("fileName") String fileName) {
        List<Document> documents = documentService.findDocumentByFileName(fileName);
        return documents.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(documents);
    }

    @GetMapping("/content/{fileName}")
    public ResponseEntity<String> getFileContent(@PathVariable("fileName") String fileName) {
        try {
            String content = documentService.getFileContent(fileName);
            return ResponseEntity.ok(content);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
