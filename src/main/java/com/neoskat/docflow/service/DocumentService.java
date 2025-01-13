package com.neoskat.docflow.service;

import com.neoskat.docflow.enums.Role;
import com.neoskat.docflow.model.DocumentUpdateRequest;
import com.neoskat.docflow.model.User;
import com.neoskat.docflow.repository.DocumentRepository;
import com.neoskat.docflow.model.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final LuceneSearchService luceneSearchService;

    private final String uploadDir = "uploads/";

    public void uploadAndIndexFile(MultipartFile file) throws IOException {

        Path path = Paths.get(uploadDir + file.getOriginalFilename());
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        Document metadata = new Document();
        metadata.setFileName(file.getOriginalFilename());
        metadata.setFilePath(path.toString());
        metadata.setFileSize(file.getSize());
        documentRepository.save(metadata);

        luceneSearchService.indexFile(path.toString(), file.getOriginalFilename());
    }

    public List<String> searchDocuments(String query) throws Exception {
        return luceneSearchService.searchDocuments(query);
    }

    public List<Document> findDocumentByFileName(String fileName) {
        System.out.println("Searching for fileName in service: " + fileName);
        return documentRepository.findByFileName(fileName);
    }

    public String getFileContent(String fileName) throws IOException {
        Path path = Paths.get(uploadDir + fileName);
        if (Files.exists(path)) {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);  // Чтение текста из файла
        } else {
            throw new IOException("File not found");
        }
    }
    public void deleteDocument(Long id) throws Exception {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new Exception("Document not found"));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser.getRole() == Role.SUPER_ADMIN || currentUser.getRole() == Role.ADMIN) {
            Files.deleteIfExists(Paths.get(doc.getFilePath()));
            documentRepository.delete(doc);
        } else if (currentUser.getRole() == Role.MANAGER) {
            if (doc.getOwner().getId().equals(currentUser.getId())) {
                Files.deleteIfExists(Paths.get(doc.getFilePath()));
                documentRepository.delete(doc);
            } else {
                throw new Exception("Нет прав на удаление этого документа");
            }
        } else {
            throw new Exception("Нет прав на удаление этого документа");
        }
    }

    public void updateDocument(Long id, DocumentUpdateRequest request) throws Exception {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new Exception("Документ не найден"));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser.getRole() == Role.SUPER_ADMIN || currentUser.getRole() == Role.ADMIN) {
            Path path = Paths.get(doc.getFilePath());
            Files.write(path, request.getNewContent().getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
        } else if (currentUser.getRole() == Role.MANAGER) {
            if (doc.getOwner().getId().equals(currentUser.getId())) {
                Path path = Paths.get(doc.getFilePath());
                Files.write(path, request.getNewContent().getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                throw new Exception("Нет прав на обновление этого документа");
            }
        } else {
            throw new Exception("Нет прав на обновление этого документа");
        }
    }
}

