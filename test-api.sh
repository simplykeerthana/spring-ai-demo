#!/bin/bash

# Spring AI Demo - Quick Test Script

BASE_URL="http://localhost:8080"

echo "🚀 Spring AI Demo - Quick Test Script"
echo "======================================"
echo ""

# Check if server is running
echo "📡 Checking if server is running..."
if ! curl -s "${BASE_URL}/actuator/health" > /dev/null 2>&1; then
    echo "❌ Server is not running at ${BASE_URL}"
    echo "Please start the application with: mvn spring-boot:run"
    exit 1
fi
echo "✅ Server is running!"
echo ""

# Test 1: Simple Chat
echo "💬 Test 1: Simple Chat"
echo "----------------------"
curl -s "${BASE_URL}/api/chat/simple?message=Say%20hello" | jq '.'
echo ""
echo ""

# Test 2: Structured Output - Weather
echo "🌤️  Test 2: Structured Output - Weather"
echo "--------------------------------------"
curl -s "${BASE_URL}/api/structured/weather?city=Tokyo" | jq '.'
echo ""
echo ""

# Test 3: Structured Output - Books
echo "📚 Test 3: Structured Output - Books"
echo "------------------------------------"
curl -s "${BASE_URL}/api/structured/books?genre=mystery&count=2" | jq '.'
echo ""
echo ""

# Test 4: Function Calling - Weather
echo "🎯 Test 4: Function Calling - Weather"
echo "-------------------------------------"
curl -s "${BASE_URL}/api/functions/weather?question=What%27s%20the%20weather%20in%20Paris?" | jq '.'
echo ""
echo ""

# Test 5: RAG - Add Knowledge
echo "📝 Test 5: RAG - Add Knowledge"
echo "-------------------------------"
curl -s -X POST "${BASE_URL}/api/rag/add" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring AI Info",
    "content": "Spring AI is a framework that provides abstractions for integrating AI models into Spring applications. It supports multiple AI providers like OpenAI, Anthropic, and Ollama. Key features include chat models, embeddings, vector stores, and function calling."
  }' | jq '.'
echo ""
echo ""

# Test 6: RAG - Query Knowledge
echo "❓ Test 6: RAG - Query Knowledge"
echo "--------------------------------"
curl -s "${BASE_URL}/api/rag/query?question=What%20is%20Spring%20AI?" | jq '.'
echo ""
echo ""

echo "✨ All tests completed!"
echo "For more detailed testing, see README.md"
