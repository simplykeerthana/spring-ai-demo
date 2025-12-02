package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * Demo 5: ChatClient API with Advisors
 * Demonstrates the fluent ChatClient API and conversation memory
 */
@RestController
@RequestMapping("/api/chatclient")
public class ChatClientController {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public ChatClientController(ChatClient.Builder chatClientBuilder) {
        this.chatMemory = new InMemoryChatMemory();
        this.chatClient = chatClientBuilder
            .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
            .build();
    }

    /**
     * Simple chat using ChatClient fluent API
     * Example: GET /api/chatclient/ask?question=What is Java?
     */
    @GetMapping("/ask")
    public Map<String, String> ask(@RequestParam String question) {
        String response = chatClient.prompt()
            .user(question)
            .call()
            .content();

        return Map.of(
            "question", question,
            "answer", response
        );
    }

    /**
     * Chat with system prompt using fluent API
     * Example: POST /api/chatclient/assistant
     * Body: { "role": "Java expert", "question": "Explain generics" }
     */
    @PostMapping("/assistant")
    public Map<String, String> assistant(@RequestBody Map<String, String> request) {
        String role = request.get("role");
        String question = request.get("question");

        String response = chatClient.prompt()
            .system("You are a {role}. Provide detailed, accurate answers.")
            .user(question)
            .call()
            .content();

        return Map.of(
            "role", role,
            "question", question,
            "answer", response
        );
    }

    /**
     * Chat with conversation memory
     * The conversation is tracked by conversationId
     * Example: POST /api/chatclient/conversation
     * Body: { "conversationId": "user-123", "message": "My name is Alice" }
     */
    @PostMapping("/conversation")
    public Map<String, Object> conversation(@RequestBody Map<String, String> request) {
        String conversationId = request.getOrDefault("conversationId", UUID.randomUUID().toString());
        String message = request.get("message");

        String response = chatClient.prompt()
            .user(message)
            .advisors(advisor -> advisor
                .param(MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId))
            .call()
            .content();

        return Map.of(
            "conversationId", conversationId,
            "message", message,
            "response", response
        );
    }

    /**
     * Clear conversation memory for a user
     * Example: DELETE /api/chatclient/conversation/user-123
     */
    @DeleteMapping("/conversation/{conversationId}")
    public Map<String, String> clearConversation(@PathVariable String conversationId) {
        chatMemory.clear(conversationId);
        return Map.of(
            "status", "success",
            "message", "Conversation cleared",
            "conversationId", conversationId
        );
    }

    /**
     * Streaming response using ChatClient
     * Example: GET /api/chatclient/stream?question=Write a story about a robot
     */
    @GetMapping(value = "/stream", produces = "text/event-stream")
    public reactor.core.publisher.Flux<String> stream(@RequestParam String question) {
        return chatClient.prompt()
            .user(question)
            .stream()
            .content();
    }
}
