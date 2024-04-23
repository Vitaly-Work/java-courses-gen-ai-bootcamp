# GenAI Bootcamp - Lab 5: Embedding

## Prerequisites
* Requires JDK17
* Requires Docker

## Application Setup

### 1. Run PGVector
`docker-compose up`
* test with opening pgadmin `http://localhost:5050. Login is `pgadmin4@pgadmin.org` and password is `admin`.

### 2. Connect to LLM
* Option A: set your own Azure OpenAI key and URL to the system variables
  * `OPENAI_API_KEY` = _your key_
  * `OPENAI_API_URL` = _your url_
  * _note_ properties `client-azureopenai-*` must be enabled
  * _note_ dependecy `spring-ai-azure-openai-spring-boot-starter` must be added to `pom.xml`
  
* Option B: use local LLM. 
  * For example Ollama.
    * [Docs](https://ollama.com/)
    * install and run Ollama
      ```
      # https://github.com/ollama/ollama
      curl -fsSL https://ollama.com/install.sh | sh
      # https://hub.docker.com/r/ollama/ollama
      docker run -d -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama
      ```
    * set properties in `application.properties`
      ```
      spring.ai.ollama.base-url=http://localhost:11434
      spring.ai.ollama.embedding.options.model=llama3
      ```
    * _note_ ```client-azureopenai-*``` properties must be disabled, dependency `spring-ai-azure-openai-spring-boot-starter` must be removed from `pom.xml` 
    * _note_ dependecy `spring-ai-ollama-spring-boot-starter` must be added to `pom.xml`

### 3. Launch the application
* `./mvnw spring-boot:run`
* test with 
`curl --location --request GET 'localhost:8080/ai/dimensions'`
`curl --location --request GET 'http://localhost:8080/ai/embedding?message=do-vector-pls'`
`curl --location --request POST 'localhost:8080/ai/embedding' \
  --header 'Content-Type: application/json' \
  --data '{"field":"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry'\''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."}'`
`curl --location --request GET 'localhost:8080/ai/embedding/search?message=lorem'`

  
## API
[OpenAPI spec](openapi.yml)
