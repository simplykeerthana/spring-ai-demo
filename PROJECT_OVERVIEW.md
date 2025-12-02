# Spring AI Demo - Project Overview

## 🎯 What This Project Demonstrates

This is a comprehensive demonstration of **Spring AI** capabilities, showcasing how to integrate AI models into Spring Boot applications. The project includes working examples of all major Spring AI features.

## ✨ Key Features Implemented

### 1. **Chat Completion** 💬
- Basic text generation
- Streaming responses (Server-Sent Events)
- System prompts for AI behavior control
- Multi-turn conversations with context

### 2. **Structured Output** 📊
- Type-safe conversion of AI responses to Java POJOs
- `BeanOutputConverter` for automatic JSON mapping
- Examples: Weather data, Book recommendations, Recipes

### 3. **Function Calling** 🔧
- AI models calling Java methods autonomously
- Real-time data integration
- Multi-function support (AI chooses which function to call)
- Examples: Weather service, Calculator

### 4. **RAG (Retrieval Augmented Generation)** 📚
- Document upload and indexing
- Vector-based similarity search
- Context-aware question answering
- In-memory vector store with embeddings

### 5. **ChatClient API** 🚀
- Fluent API for chat interactions
- Conversation memory (tracks context across messages)
- Advisors pattern for chat enhancement
- Role-based assistants

## 📁 Project Structure

```
spring-ai-demo/
├── src/main/java/com/example/springai/
│   ├── SpringAiDemoApplication.java          # Main application
│   ├── config/
│   │   └── SpringAiConfig.java               # AI configuration
│   ├── controller/
│   │   ├── ChatController.java               # Basic chat endpoints
│   │   ├── StructuredOutputController.java   # POJO conversion
│   │   ├── FunctionCallingController.java    # Function calling
│   │   ├── RagController.java                # Document Q&A
│   │   └── ChatClientController.java         # Fluent API
│   ├── service/
│   │   ├── WeatherService.java               # Weather data provider
│   │   └── RagService.java                   # RAG implementation
│   └── model/
│       ├── WeatherResponse.java              # Weather POJO
│       ├── BookRecommendation.java           # Book POJO
│       └── RecipeResponse.java               # Recipe POJO
├── src/main/resources/
│   └── application.yml                        # Configuration
├── test-documents/                            # Sample documents for RAG
│   ├── spring-framework.txt
│   └── ai-basics.txt
├── pom.xml                                    # Maven dependencies
├── README.md                                  # Main documentation
├── QUICKSTART.md                              # Quick setup guide
├── EXAMPLES.md                                # API usage examples
├── test-api.sh                                # Test script
└── postman-collection.json                    # Postman API collection

```

## 🚀 Quick Start (3 Steps)

### 1. Install Ollama (Free, Local AI)
```bash
# macOS
brew install ollama

# Start Ollama
ollama serve

# Pull models (in another terminal)
ollama pull llama3.2
ollama pull nomic-embed-text
```

### 2. Run the Application
```bash
mvn spring-boot:run
```

### 3. Test It!
```bash
# Simple test
curl "http://localhost:8080/api/chat/simple?message=Hello"

# Or run all tests
./test-api.sh
```

## 🎨 API Endpoints

### Chat Completion (`/api/chat`)
- `GET /api/chat/simple` - Simple chat
- `POST /api/chat/system` - Chat with system prompt
- `GET /api/chat/stream` - Streaming responses
- `POST /api/chat/conversation` - Multi-turn conversation

### Structured Output (`/api/structured`)
- `GET /api/structured/weather` - Weather data as JSON
- `GET /api/structured/books` - Book recommendations
- `GET /api/structured/recipe` - Recipe with ingredients

### Function Calling (`/api/functions`)
- `GET /api/functions/weather` - AI calls weather service
- `GET /api/functions/assistant` - AI chooses functions

### RAG (`/api/rag`)
- `POST /api/rag/add` - Add content to knowledge base
- `POST /api/rag/upload` - Upload document
- `GET /api/rag/query` - Query documents
- `DELETE /api/rag/clear` - Clear knowledge base

### ChatClient API (`/api/chatclient`)
- `GET /api/chatclient/ask` - Simple question
- `POST /api/chatclient/assistant` - Role-based assistant
- `POST /api/chatclient/conversation` - Conversation with memory
- `DELETE /api/chatclient/conversation/{id}` - Clear memory
- `GET /api/chatclient/stream` - Streaming response

## 🛠️ Technologies Used

- **Spring Boot 3.3.5** - Application framework
- **Spring AI 1.0.0-M4** - AI integration
- **Ollama** - Local AI models (or OpenAI)
- **Java 17** - Programming language
- **Maven** - Build tool

## 📖 Documentation Files

1. **README.md** - Comprehensive guide with all features
2. **QUICKSTART.md** - Get started in minutes
3. **EXAMPLES.md** - Detailed API usage examples
4. **postman-collection.json** - Import into Postman for testing

## 🔥 Highlights

### Why This Demo is Valuable

1. **Production-Ready Code**: Not just examples, but structured, maintainable code
2. **Multiple AI Providers**: Works with Ollama (free, local) or OpenAI
3. **Real-World Use Cases**: 
   - Customer support chatbots
   - Document Q&A systems
   - Data extraction from unstructured text
   - Intelligent assistants
4. **Best Practices**: 
   - Proper separation of concerns
   - Type-safe POJOs
   - Error handling
   - Clean architecture

### Learning Outcomes

After exploring this project, you'll understand:
- How to integrate AI models into Spring Boot
- Different patterns for AI interactions (chat, functions, RAG)
- How to make AI responses type-safe
- How to give AI access to real-time data
- How to build Q&A systems over your documents
- How to maintain conversation context

## 🎯 Next Steps to Explore

1. **Vector Databases**: Replace SimpleVectorStore with pgvector, Pinecone, or Weaviate
2. **Image Generation**: Add text-to-image capabilities
3. **Audio**: Add speech-to-text and text-to-speech
4. **Advanced RAG**: Implement re-ranking, hybrid search
5. **Production Features**: Add caching, rate limiting, authentication
6. **Observability**: Add metrics, tracing, monitoring

## 📚 Resources

- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/index.html)
- [Ollama Models](https://ollama.ai/library)
- [OpenAI API](https://platform.openai.com/docs)

## 🤝 Contributing Ideas

Want to extend this demo? Consider adding:
- Image generation endpoints
- Audio transcription
- Multi-modal inputs (text + images)
- Chat UI frontend
- Database persistence for conversations
- WebSocket support for real-time chat
- Authentication and user management

## 📝 License

MIT - Feel free to use this for learning or as a starter for your projects!

---

**Created to demonstrate Spring AI capabilities** 🚀

Explore the code, run the examples, and build amazing AI-powered applications with Spring!
