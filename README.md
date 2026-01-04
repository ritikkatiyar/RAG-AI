🧠 GenAI Spring Boot Application
RAG + Tool Calling using Spring AI, Ollama & PgVector

This project demonstrates how to build a local GenAI application using Spring Boot + Spring AI with:

🔹 Ollama for LLM inference (local models)

🔹 PgVector (PostgreSQL) for vector storage

🔹 RAG (Retrieval Augmented Generation) using PDF ingestion

🔹 Tool / Function Calling using Spring AI

🔹 Docker Compose for infra setup

🏗️ Architecture Overview
User Request
↓
Spring Boot Controller
↓
ChatClient (Spring AI)
↓
LLM (Ollama - llama3.1)
↓
↳ Tool Calling (Weather API mock)
↳ Vector Search (PgVector)
↓
Final Answer

📦 Tech Stack
Component	Technology
Language	Java 21
Framework	Spring Boot 3.5
AI Framework	Spring AI 1.1.2
LLM Runtime	Ollama
Chat Model	llama3.1
Embeddings	nomic-embed-text
Vector DB	PostgreSQL + pgvector
Infra	Docker, Docker Compose
✅ Prerequisites

Make sure you have the following installed:

Java 21

Docker & Docker Compose

Ollama

Maven

Git

🔧 Step 1: Clone the Repository
git clone https://github.com/<your-username>/<your-repo>.git
cd genai

🐳 Step 2: Start Infrastructure (Postgres + PgVector)
docker-compose.yml
version: "3.9"

services:
pgvector:
image: pgvector/pgvector:pg16
container_name: pgvector
ports:
- "5432:5432"
environment:
POSTGRES_DB: mydatabase
POSTGRES_USER: myuser
POSTGRES_PASSWORD: secret
volumes:
- pgvector-data:/var/lib/postgresql/data

volumes:
pgvector-data:

Start containers
docker compose up -d


Verify:

docker ps

🤖 Step 3: Setup Ollama Models
Install required models
ollama pull llama3.1
ollama pull nomic-embed-text


Verify:

ollama list

⚠️ IMPORTANT MODEL NOTE
Model	Tool Support
gemma:2b	❌ No
phi3	❌ No
llama3 / llama3.1	✅ Yes

👉 Tool calling WILL NOT work with gemma or phi3

⚙️ Step 4: Application Configuration
application.yml
spring:
application:
name: genai

datasource:
url: jdbc:postgresql://localhost:5432/mydatabase
username: myuser
password: secret
driver-class-name: org.postgresql.Driver

ai:
ollama:
base-url: http://localhost:11434

      chat:
        options:
          model: llama3.1
          temperature: 0.2

      embedding:
        options:
          model: nomic-embed-text

    vectorstore:
      pgvector:
        table-name: embeddings
        dimensions: 768
        initialize-schema: true

logging:
level:
org.springframework.ai: DEBUG

🗂️ Step 5: PDF Ingestion (RAG)

Place your PDF in:

src/main/resources/India_Constitution.pdf


On application startup:

PDF is split into chunks

Embedded using nomic-embed-text

Stored in pgvector

▶️ Step 6: Run the Application
./mvnw spring-boot:run


OR

mvn spring-boot:run

🔍 Verify Database

Connect to Postgres:

docker exec -it pgvector psql -U myuser -d mydatabase


List tables:

\dt


Check embeddings count:

SELECT COUNT(*) FROM embeddings;

🌤️ Tool Calling Example (Weather)
Endpoint
GET /weather?message=What is the weather in Gurugram?

What happens:

LLM detects weather intent

Calls Java function via Spring AI ToolCallback

Tool returns response

LLM formats final answer

📚 RAG Example (Indian Constitution)
GET /law?question=What is Article 21?

What happens:

Similar documents retrieved from PgVector

Context injected into prompt

LLM answers grounded in source data

🧪 Debugging Tips
Enable AI logs
logging:
level:
org.springframework.ai: DEBUG


Look for:

Invoking tool: currentWeather

🚨 Common Errors & Fixes
❌ gemma does not support tools

➡️ Switch to llama3.1

❌ JDBC password authentication failed

➡️ Check Docker env vars match application.yml

❌ embeddings table empty

➡️ Ensure embedding model is pulled and running

📌 Key Learnings

Tool calling is model-dependent

RAG requires embedding models

Spring AI 1.1.x uses ToolCallbacks, not @Tool

Ollama allows fully local GenAI pipelines

🚀 Future Enhancements

Real weather API integration

Streaming chat responses

Multi-tool orchestration

Authentication & rate limiting

UI with React / Next.js

🙌 Credits

Built using Spring AI, Ollama, and PgVector
Inspired by real-world GenAI production patterns.

⭐ If you found this useful

Give the repo a ⭐ and feel free to fork & extend!