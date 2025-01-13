package com.neoskat.docflow.controller;

import com.neoskat.docflow.model.DocumentUpdateRequest;
import com.neoskat.docflow.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final DocumentService documentService;

    @PutMapping("/documents/{id}")
    public ResponseEntity<String> updateDocument(@PathVariable Long id, @RequestBody DocumentUpdateRequest request) {
        try {
            documentService.updateDocument(id, request);
            return ResponseEntity.ok("Документ обновлен");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

