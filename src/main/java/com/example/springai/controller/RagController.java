package com.example.springai.controller;

import com.example.springai.service.RagService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Demo 4: RAG (Retrieval Augmented Generation)
 * Demonstrates chat with your documents
 */
@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    /**
     * Upload and index a text document
     * Example: POST /api/rag/upload (with file in form-data)
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            String content = new String(file.getBytes());
            ragService.indexDocument(file.getOriginalFilename(), content);
            
            return Map.of(
                "status", "success",
                "message", "Document indexed successfully",
                "filename", file.getOriginalFilename()
            );
        } catch (Exception e) {
            return Map.of(
                "status", "error",
                "message", e.getMessage()
            );
        }
    }

    /**
     * Add text content directly to the knowledge base
     * Example: POST /api/rag/add
     * Body: { "title": "Spring Boot Guide", "content": "Spring Boot is..." }
     */
    @PostMapping("/add")
    public Map<String, String> addContent(@RequestBody Map<String, String> request) {
        String title = request.get("title");
        String content = request.get("content");
        
        ragService.indexDocument(title, content);
        
        return Map.of(
            "status", "success",
            "message", "Content indexed successfully",
            "title", title
        );
    }

    /**
     * Query the knowledge base
     * Example: GET /api/rag/query?question=What is Spring Boot?
     */
    @GetMapping("/query")
    public Map<String, Object> query(@RequestParam String question) {
        String answer = ragService.queryDocuments(question);
        
        return Map.of(
            "question", question,
            "answer", answer
        );
    }

    /**
     * Clear the knowledge base
     * Example: DELETE /api/rag/clear
     */
    @DeleteMapping("/clear")
    public Map<String, String> clearKnowledgeBase() {
        ragService.clearDocuments();
        return Map.of(
            "status", "success",
            "message", "Knowledge base cleared"
        );
    }
}
