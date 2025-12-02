# Spring AI Demo Application

A hands-on demo showcasing 5 powerful AI patterns using Spring AI with Ollama: Basic Chat, Structured Output, Function Calling, RAG (Document Q&A), and ChatClient with Memory.

## Quick Start (3 Steps)

### 1. Install Ollama & Pull Models
```bash
# Install Ollama from https://ollama.ai/
# Then pull the required models:
ollama pull llama3.2
ollama pull nomic-embed-text

# Start Ollama service (keep this running)
ollama serve
```

### 2. Clone & Build
```bash
git clone https://github.com/simplykeerthana/spring-ai-demo.git
cd spring-ai-demo
# Build the project (creates the JAR file)
mvn clean package
```


### 3. Run the App
```bash
# Option 1: Run with Maven (easiest)
mvn spring-boot:run

# Option 2: Run the generated JAR
java -jar target/spring-ai-demo-0.0.1-SNAPSHOT.jar
```


App starts at: `http://localhost:8080`

## Test the Features

### Basic Chat
```bash
# Ask a question
curl "http://localhost:8080/api/chat?question=What%20is%20Spring%20AI?"

# Stream response
curl "http://localhost:8080/api/chat/stream?question=Tell%20me%20about%20AI"
```

### Structured Output (Type-Safe POJOs)
```bash
# Weather data
curl "http://localhost:8080/api/structured/weather?location=Paris"

# Book recommendations
curl "http://localhost:8080/api/structured/book?genre=mystery"

# Recipe generation
curl "http://localhost:8080/api/structured/recipe?cuisine=Thai&dietaryRestriction=vegan"
```

### Function Calling (AI Invokes Java Methods)
```bash
# AI uses calculator
curl "http://localhost:8080/api/function/calculate?question=What%20is%2015%20times%2023?"

# AI checks weather
curl "http://localhost:8080/api/function/weather?question=Weather%20in%20Tokyo?"
```

### RAG - Document Q&A
```bash
# Index document
curl -X POST "http://localhost:8080/api/rag/index" \
  -H "Content-Type: application/json" \
  -d '{"content": "Spring AI makes building AI apps in Java easy and production-ready."}'

# Ask questions about indexed documents
curl "http://localhost:8080/api/rag/query?question=What%20does%20Spring%20AI%20do?"

# List all documents
curl "http://localhost:8080/api/rag/documents"
```

### ChatClient with Conversation Memory
```bash
# First message (save the conversationId from response)
curl -X POST "http://localhost:8080/api/chatclient/conversation" \
  -H "Content-Type: application/json" \
  -d '{"conversationId": "user-123", "message": "My name is Alice"}'

# Follow-up with same conversationId (remembers context!)
curl -X POST "http://localhost:8080/api/chatclient/conversation" \
  -H "Content-Type: application/json" \
  -d '{"conversationId": "user-123", "message": "What is my name?"}'

# Clear conversation memory
curl -X DELETE "http://localhost:8080/api/chatclient/conversation/user-123"
```

## What's Inside

- **5 Controllers**: Each demonstrating a different AI pattern
- **3 Services**: Weather mock service + RAG vector store operations
- **3 Models**: Type-safe POJOs (Weather, Book, Recipe)
- **18+ API Endpoints**: coverage of Spring AI capabilities

## Prerequisites

- **Java 17+**
- **Maven 3.6+**
- **Ollama** (with `llama3.2` and `nomic-embed-text` models)


##  Common Issues

**"Connection refused" error?**
→ Start Ollama: `ollama serve`

**"Model not found"?**
→ Pull models: `ollama pull llama3.2 && ollama pull nomic-embed-text`

**Slow first response?**
→ Normal! Ollama loads model on first request

**Want faster responses?**
→ Use GPU-accelerated Ollama or smaller models

## Real-World Use Cases

1.  Chatbots & customer support
2.  Content generation (blogs, summaries)
3.  Document analysis & search
4.  Data extraction & classification
  

## Learn More

- [Spring AI Docs](https://docs.spring.io/spring-ai/reference/)
- [Ollama Docs](https://ollama.ai/)
- [GitHub](https://github.com/spring-projects/spring-ai)

---

## ☕ Support This Project

Found this demo helpful? Consider buying me a coffee!

[![Buy Me A Coffee](https://img.shields.io/badge/Buy%20Me%20A%20Coffee-Support-yellow?style=for-the-badge&logo=buy-me-a-coffee)](https://buymeacoffee.com/simplykeerthana)

Your support helps me create more open-source tutorials and demos! 

---

**Ready to build AI-powered Spring apps? Clone, run, and explore!** 

⭐ **Star this repo** if you found it useful!
