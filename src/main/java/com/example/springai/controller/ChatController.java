package com.example.springai.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * Demo 1: Basic Chat Completion
 * Demonstrates simple text generation and streaming responses
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatModel chatModel;

    public ChatController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * Simple chat endpoint
     * Example: GET /api/chat/simple?message=Tell me a joke
     */
    @GetMapping("/simple")
    public Map<String, String> simpleChat(@RequestParam String message) {
        String response = chatModel.call(message);
        return Map.of(
            "question", message,
            "answer", response
        );
    }

    /**
     * Chat with system prompt
     * Example: POST /api/chat/system
     * Body: { "message": "What is Java?" }
     */
    @PostMapping("/system")
    public Map<String, String> chatWithSystem(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        
        Prompt prompt = new Prompt(List.of(
            new org.springframework.ai.chat.messages.SystemMessage(
                "You are a helpful AI assistant specializing in software development. " +
                "Provide concise, accurate answers with code examples when relevant."
            ),
            new UserMessage(userMessage)
        ));
        
        ChatResponse response = chatModel.call(prompt);
        
        return Map.of(
            "question", userMessage,
            "answer", response.getResult().getOutput().getContent(),
            "model", response.getMetadata().getModel(),
            "tokensUsed", String.valueOf(response.getMetadata().getUsage().getTotalTokens())
        );
    }

    /**
     * Streaming chat endpoint
     * Example: GET /api/chat/stream?message=Write a haiku about coding
     * Returns: Server-Sent Events (SSE) stream
     */
    @GetMapping(value = "/stream", produces = "text/event-stream")
    public Flux<String> streamChat(@RequestParam String message) {
        Prompt prompt = new Prompt(message);
        return chatModel.stream(prompt)
            .map(chatResponse -> chatResponse.getResult().getOutput().getContent());
    }

    /**
     * Conversation with context
     * Example: POST /api/chat/conversation
     * Body: { "messages": ["Hi, I'm working on a Spring project", "Can you help me with REST APIs?"] }
     */
    @PostMapping("/conversation")
    public Map<String, Object> conversation(@RequestBody Map<String, List<String>> request) {
        List<String> messageTexts = request.get("messages");
        
        List<Message> messages = messageTexts.stream()
            .map(UserMessage::new)
            .collect(java.util.stream.Collectors.toList());
        
        Prompt prompt = new Prompt(messages);
        ChatResponse response = chatModel.call(prompt);
        
        return Map.of(
            "conversation", messageTexts,
            "response", response.getResult().getOutput().getContent()
        );
    }
}
