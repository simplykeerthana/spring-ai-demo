# Troubleshooting Guide

Common issues and their solutions when running the Spring AI Demo.

## Installation Issues

### 1. Ollama Not Found

**Error:**
```
Connection refused: localhost:11434
```

**Solution:**
```bash
# Install Ollama
brew install ollama  # macOS
# or download from https://ollama.ai

# Start Ollama service
ollama serve
```

### 2. Model Not Available

**Error:**
```
model 'llama3.2' not found
```

**Solution:**
```bash
# Pull the required models
ollama pull llama3.2
ollama pull nomic-embed-text

# Verify models are installed
ollama list
```

### 3. Java Version Issues

**Error:**
```
Java version not supported
```

**Solution:**
```bash
# Check Java version (need 17+)
java -version

# Install Java 17 if needed
brew install openjdk@17  # macOS

# Set JAVA_HOME
export JAVA_HOME=/usr/local/opt/openjdk@17
```

## Build Issues

### 1. Maven Dependencies Not Downloading

**Error:**
```
Could not resolve dependencies
```

**Solution:**
```bash
# Clear Maven cache
rm -rf ~/.m2/repository/org/springframework/ai

# Force update
mvn clean install -U

# If still failing, check internet connection and Maven settings
```

### 2. Spring AI Version Issues

**Error:**
```
Could not find artifact org.springframework.ai
```

**Solution:**
Make sure `pom.xml` has the milestone repository:
```xml
<repositories>
    <repository>
        <id>spring-milestones</id>
        <name>Spring Milestones</name>
        <url>https://repo.spring.io/milestone</url>
    </repository>
</repositories>
```

## Runtime Issues

### 1. Port Already in Use

**Error:**
```
Port 8080 is already in use
```

**Solution:**
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or change port in application.yml
server:
  port: 8081
```

### 2. Out of Memory

**Error:**
```
java.lang.OutOfMemoryError: Java heap space
```

**Solution:**
```bash
# Increase heap size
export MAVEN_OPTS="-Xmx2g -Xms512m"
mvn spring-boot:run

# Or run with specific memory settings
java -Xmx2g -jar target/spring-ai-demo-0.0.1-SNAPSHOT.jar
```

### 3. Connection Timeout with Ollama

**Error:**
```
Connection timeout to localhost:11434
```

**Solution:**
```bash
# Check if Ollama is running
ollama list

# If not running, start it
ollama serve

# Check if models are loaded
ollama ps

# Test Ollama directly
curl http://localhost:11434/api/generate -d '{
  "model": "llama3.2",
  "prompt": "Hello"
}'
```

## API Issues

### 1. 404 Not Found

**Error:**
```
404: Not Found for /api/chat/simple
```

**Solution:**
- Ensure application started successfully
- Check logs for errors during startup
- Verify endpoint path is correct
- Check that controller is being scanned:
```java
@SpringBootApplication
public class SpringAiDemoApplication {
    // Should scan com.example.springai package
}
```

### 2. AI Responses Are Slow

**Issue:** Responses taking too long

**Solutions:**
1. **Use smaller models:**
```bash
ollama pull llama3.2:1b  # Smaller, faster model
```

2. **Update application.yml:**
```yaml
spring:
  ai:
    ollama:
      chat:
        options:
          model: llama3.2:1b
          num-ctx: 2048  # Reduce context window
```

3. **Use streaming endpoints** for better UX

### 3. RAG Not Finding Documents

**Issue:** Queries return "No relevant information found"

**Solutions:**
1. Verify documents were indexed:
```bash
curl -X POST http://localhost:8080/api/rag/add \
  -H "Content-Type: application/json" \
  -d '{"title": "Test", "content": "Test content"}'
```

2. Check embedding model is running:
```bash
ollama pull nomic-embed-text
```

3. Try more specific queries matching document content

### 4. Function Calling Not Working

**Issue:** AI not calling functions

**Solutions:**
1. Make sure you're using a model that supports function calling
2. Use OpenAI instead of Ollama if function calling is critical:
```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
```

3. Be explicit in your questions:
```bash
# Instead of: "Tokyo"
# Use: "What's the weather in Tokyo?"
```

## Configuration Issues

### 1. OpenAI API Key Not Working

**Error:**
```
401 Unauthorized
```

**Solution:**
```bash
# Set environment variable
export OPENAI_API_KEY=sk-...

# Or add to application.yml
spring:
  ai:
    openai:
      api-key: sk-your-key-here

# Or use .env file (not committed to git)
```

### 2. Wrong Model Configuration

**Error:**
```
Model 'gpt-5' not found
```

**Solution:**
Check available models and use correct names:
- OpenAI: `gpt-4`, `gpt-3.5-turbo`
- Ollama: `llama3.2`, `llama2`, `mistral`

## Testing Issues

### 1. Test Script Fails

**Error:**
```bash
./test-api.sh: command not found: jq
```

**Solution:**
```bash
# Install jq for JSON parsing
brew install jq  # macOS
sudo apt install jq  # Linux

# Or test without jq
curl http://localhost:8080/api/chat/simple?message=Hello
```

### 2. Postman Collection Import Issues

**Solution:**
1. Open Postman
2. Click Import
3. Select `postman-collection.json`
4. Verify `baseUrl` variable is set to `http://localhost:8080`

## Performance Optimization

### 1. Slow First Request

**Issue:** First request takes long

**Explanation:** Model loading time

**Solution:**
- Normal behavior, subsequent requests will be faster
- Use keep-alive in Ollama
- Consider model pre-loading

### 2. Memory Usage

**Issue:** High memory consumption

**Solutions:**
```bash
# Monitor Java memory
jps -v

# Adjust heap size
export MAVEN_OPTS="-Xmx1g"

# Use smaller models
ollama pull llama3.2:1b
```

### 3. Concurrent Requests

**Issue:** Multiple simultaneous requests fail

**Solutions:**
1. Increase Ollama's concurrent request limit
2. Implement request queuing in application
3. Use async processing
4. Scale Ollama horizontally

## Logging and Debugging

### Enable Debug Logging

Update `application.yml`:
```yaml
logging:
  level:
    org.springframework.ai: DEBUG
    com.example.springai: DEBUG
```

### Check Application Logs

```bash
# During development
mvn spring-boot:run

# Specific log level
mvn spring-boot:run -Dspring-boot.run.arguments="--logging.level.org.springframework.ai=TRACE"
```

### Test Ollama Directly

```bash
# Check health
curl http://localhost:11434/

# List models
ollama list

# Test generation
ollama run llama3.2 "Hello"
```

## Common Mistakes

1. **Forgetting to start Ollama** - Always run `ollama serve` first
2. **Not pulling models** - Run `ollama pull llama3.2` before using
3. **Wrong endpoint URLs** - Check the path carefully
4. **Missing Content-Type header** - POST requests need `application/json`
5. **Not encoding URL parameters** - Use `%20` for spaces in GET requests

## Still Having Issues?

1. **Check logs:** Look for error messages in application startup
2. **Verify setup:** Run through QUICKSTART.md again
3. **Test components individually:**
   - Ollama: `ollama list`
   - Java: `java -version`
   - Maven: `mvn -version`
   - Application: `curl http://localhost:8080/actuator/health`

4. **Create minimal test:**
```bash
# Simplest possible test
curl "http://localhost:8080/api/chat/simple?message=hi"
```

## Getting Help

- Check Spring AI docs: https://docs.spring.io/spring-ai/reference/
- Ollama docs: https://github.com/ollama/ollama
- Spring Boot docs: https://spring.io/projects/spring-boot

