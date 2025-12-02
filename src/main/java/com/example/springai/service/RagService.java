package com.example.springai.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RAG Service for document-based question answering
 */
@Service
public class RagService {

    private final ChatModel chatModel;
    private final VectorStore vectorStore;
    private final List<Document> documents;

    public RagService(ChatModel chatModel, EmbeddingModel embeddingModel) {
        this.chatModel = chatModel;
        this.vectorStore = new SimpleVectorStore(embeddingModel);
        this.documents = new ArrayList<>();
    }

    /**
     * Index a document into the vector store
     */
    public void indexDocument(String title, String content) {
        // Split content into chunks (simple implementation)
        List<String> chunks = splitIntoChunks(content, 500);
        
        List<Document> docs = new ArrayList<>();
        for (int i = 0; i < chunks.size(); i++) {
            Document doc = new Document(
                title + "_chunk_" + i,
                chunks.get(i),
                java.util.Map.of(
                    "title", title,
                    "chunk", String.valueOf(i)
                )
            );
            docs.add(doc);
        }
        
        documents.addAll(docs);
        vectorStore.add(docs);
    }

    /**
     * Query documents using RAG
     */
    public String queryDocuments(String question) {
        if (documents.isEmpty()) {
            return "No documents have been indexed yet. Please upload documents first.";
        }

        // Search for relevant documents
        List<Document> relevantDocs = vectorStore.similaritySearch(question);
        
        if (relevantDocs.isEmpty()) {
            return "No relevant information found in the knowledge base.";
        }

        // Build context from relevant documents
        String context = relevantDocs.stream()
            .map(Document::getContent)
            .collect(Collectors.joining("\n\n"));

        // Create prompt with context
        String prompt = String.format("""
            Answer the following question based on the provided context.
            If the answer cannot be found in the context, say so.
            
            Context:
            %s
            
            Question: %s
            
            Answer:
            """, context, question);

        return chatModel.call(prompt);
    }

    /**
     * Clear all documents from the knowledge base
     */
    public void clearDocuments() {
        documents.clear();
        // Reinitialize vector store
        // Note: SimpleVectorStore doesn't have a clear method, so we'd need to recreate it
        // For now, just clear the documents list
    }

    /**
     * Split text into chunks
     */
    private List<String> splitIntoChunks(String text, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        String[] sentences = text.split("\\. ");
        
        StringBuilder currentChunk = new StringBuilder();
        
        for (String sentence : sentences) {
            if (currentChunk.length() + sentence.length() > chunkSize) {
                if (currentChunk.length() > 0) {
                    chunks.add(currentChunk.toString().trim());
                    currentChunk = new StringBuilder();
                }
            }
            currentChunk.append(sentence).append(". ");
        }
        
        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString().trim());
        }
        
        return chunks;
    }
}
