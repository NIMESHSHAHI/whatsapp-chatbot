# 🤖 WhatsApp Chatbot Backend Simulation

[![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=flat-square&logo=docker)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)

A **REST API backend** built with Java 17 and Spring Boot 3 that simulates a WhatsApp Business chatbot webhook. Accepts JSON messages and responds with predefined replies — fully containerized with Docker for easy deployment anywhere.

---

## 📋 Table of Contents

- [Project Overview](#-project-overview)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Quick Start with Docker](#-quick-start-with-docker)
- [Run Locally without Docker](#-run-locally-without-docker)
- [API Endpoints](#-api-endpoints)
- [Bot Commands](#-bot-commands)
- [Testing](#-testing)
- [Docker Reference](#-docker-reference)
- [Push to Docker Hub](#-push-to-docker-hub)
- [Console Logging](#-console-logging)

---

## 📌 Project Overview

This project simulates a WhatsApp Business API webhook backend. It:

- Receives incoming WhatsApp-style messages via `POST /webhook`
- Matches the message text to a predefined reply map (case-insensitive)
- Returns a structured JSON response with the bot's reply
- Logs every incoming and outgoing message to the console
- Includes a webhook verification endpoint (Meta-style `hub.challenge`)
- Ships with a `Dockerfile` and `docker-compose.yml` for one-command deployment

---

## 🛠️ Tech Stack

| Technology          | Purpose                        |
|---------------------|--------------------------------|
| Java 17             | Core language                  |
| Spring Boot 3.2     | REST API framework             |
| Spring Web MVC      | Controller / routing           |
| Spring Actuator     | Health check endpoints         |
| SLF4J + Logback     | Structured message logging     |
| JUnit 5 + MockMvc   | Unit & integration tests       |
| Maven               | Build & dependency management  |
| Docker              | Containerization & deployment  |
| Docker Compose      | Multi-service orchestration    |

---

## 🏗️ Project Structure

```
whatsapp-chatbot/
├── Dockerfile                          ← Docker image definition (2-stage build)
├── docker-compose.yml                  ← One-command startup config
├── .dockerignore                       ← Files excluded from Docker build
├── pom.xml                             ← Maven dependencies
├── README.md
└── src/
    ├── main/
    │   ├── java/com/chatbot/whatsapp/
    │   │   ├── WhatsappChatbotApplication.java     ← Entry point
    │   │   ├── controller/
    │   │   │   └── WebhookController.java           ← REST endpoints
    │   │   ├── service/
    │   │   │   └── ChatbotService.java               ← Reply logic + logging
    │   │   └── model/
    │   │       ├── IncomingMessage.java              ← Request JSON model
    │   │       └── OutgoingMessage.java              ← Response JSON model
    │   └── resources/
    │       └── application.properties               ← App configuration
    └── test/
        └── java/com/chatbot/whatsapp/
            └── WhatsappChatbotApplicationTests.java  ← 9 unit + integration tests
```

---

## 🐳 Quick Start with Docker

> **Prerequisites:** [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running.

### Option 1 — Docker Compose (Recommended)

```bash
# 1. Clone the repository
git clone https://github.com/YOUR_USERNAME/whatsapp-chatbot.git
cd whatsapp-chatbot

# 2. Build and start the container
docker-compose up --build

# To run in the background (detached mode):
docker-compose up --build -d
```

App is now live at: **http://localhost:8080**

---

### Option 2 — Docker Commands

```bash
# 1. Clone the repository
git clone https://github.com/YOUR_USERNAME/whatsapp-chatbot.git
cd whatsapp-chatbot

# 2. Build the Docker image
docker build -t whatsapp-chatbot:latest .

# 3. Run the container
docker run -d \
  --name whatsapp-chatbot \
  -p 8080:8080 \
  whatsapp-chatbot:latest
```

App is now live at: **http://localhost:8080**

---

### Option 3 — Pull from Docker Hub (No build needed)

```bash
docker run -d \
  --name whatsapp-chatbot \
  -p 8080:8080 \
  YOUR_DOCKERHUB_USERNAME/whatsapp-chatbot:latest
```

> Replace `YOUR_DOCKERHUB_USERNAME` with the actual Docker Hub username.

---

### Verify the container is running

```bash
# Check container status
docker ps

# Expected output:
# CONTAINER ID  IMAGE               STATUS          PORTS
# abc123456789  whatsapp-chatbot    Up X seconds    0.0.0.0:8080->8080/tcp

# View live logs
docker logs -f whatsapp-chatbot

# Check health
curl http://localhost:8080/health
```

---

## 💻 Run Locally without Docker

> **Prerequisites:** Java 17+, Maven 3.8+

```bash
# 1. Clone the repository
git clone https://github.com/YOUR_USERNAME/whatsapp-chatbot.git
cd whatsapp-chatbot

# 2. Build the project
./mvnw clean package -DskipTests       # Mac/Linux
mvnw.cmd clean package -DskipTests    # Windows

# 3. Run the application
./mvnw spring-boot:run                 # Mac/Linux
mvnw.cmd spring-boot:run              # Windows
```

App is now live at: **http://localhost:8080**

---

## 📡 API Endpoints

### `POST /webhook` — Send a message to the bot

**Request Body:**
```json
{
  "from": "+919876543210",
  "message": "Hi",
  "messageId": "msg_001",
  "timestamp": "2026-03-25T10:00:00"
}
```

**Response:**
```json
{
  "to": "+919876543210",
  "reply": "Hello! 👋 Welcome! How can I help you today?",
  "status": "sent",
  "originalMessage": "Hi",
  "processedAt": "2026-03-25T10:00:01"
}
```

---

### `GET /webhook` — Webhook Verification (Meta-style)

Simulates the Meta WhatsApp Business API hub verification handshake.

```
GET /webhook?hub.mode=subscribe&hub.verify_token=whatsapp_bot_token_2026&hub.challenge=test123
```

Returns `200 OK` with the challenge value echoed back (`test123`).  
Returns `403 Forbidden` if the token is wrong.

---

### `GET /health` — Health Check

```bash
curl http://localhost:8080/health
```

```json
{
  "status": "UP",
  "service": "WhatsApp Chatbot Backend",
  "version": "1.0.0",
  "message": "Bot is running and ready to receive messages 🤖"
}
```

---

### `GET /` — API Info

```bash
curl http://localhost:8080/
```

Returns app name, version, available endpoints, and a sample request payload.

---

## 💬 Bot Commands

All message matching is **case-insensitive** (e.g. `HI`, `hi`, `Hi` all work).

| User sends          | Bot replies                                                         |
|---------------------|---------------------------------------------------------------------|
| `Hi`                | Hello! 👋 Welcome! How can I help you today?                       |
| `Hello`             | Hey there! 😊 How can I assist you?                                |
| `Bye`               | Goodbye! 👋 Have a great day!                                      |
| `Goodbye`           | Take care! See you soon. 😊                                        |
| `Help`              | Lists available commands                                            |
| `Thanks`            | You're welcome! 😊                                                 |
| `Thank you`         | You're most welcome! 🙌                                            |
| `How are you`       | I'm just a bot, but doing great! 🤖                                |
| `What is your name` | I'm WhatsApp Bot, your virtual assistant! 🤖                       |
| `Services`          | Lists service menu (Customer Support, Product Info, Order Tracking) |
| `1`                 | Customer Support info                                               |
| `2`                 | Product Info                                                        |
| `3`                 | Order Tracking                                                      |
| *(anything else)*   | Default fallback reply                                              |

---

## 🧪 Testing

### Test with cURL

```bash
# Test Hi → Hello
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"from":"+919876543210","message":"Hi","messageId":"msg_001","timestamp":"2026-03-25T10:00:00"}'

# Test Bye → Goodbye
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"from":"+919876543210","message":"Bye","messageId":"msg_002","timestamp":"2026-03-25T10:01:00"}'

# Health check
curl http://localhost:8080/health

# Webhook verification
curl "http://localhost:8080/webhook?hub.mode=subscribe&hub.verify_token=whatsapp_bot_token_2026&hub.challenge=test123"
```

---

### Test with Postman

1. Open Postman → New Request
2. Set method to `POST`
3. URL: `http://localhost:8080/webhook`
4. Headers: `Content-Type: application/json`
5. Body → raw → JSON → paste the request body above
6. Click **Send**

---

### Run Unit Tests

```bash
./mvnw test           # Mac/Linux
mvnw.cmd test         # Windows
```

**Test coverage:**

| Test | Description |
|------|-------------|
| `testHiReply` | Hi → Hello reply |
| `testByeReply` | Bye → Goodbye reply |
| `testDefaultReply` | Unknown message → fallback |
| `testEmptyMessage` | Empty message handled gracefully |
| `testCaseInsensitive` | HI == hi == Hi |
| `testWebhookPost` | POST /webhook returns 200 |
| `testWebhookByeMessage` | Bye via HTTP returns sent status |
| `testHealthEndpoint` | GET /health returns UP |
| `testWebhookVerification` | Valid token echoes challenge |
| `testWebhookVerificationFail` | Wrong token returns 403 |

---

## 🐳 Docker Reference

### Key commands

```bash
# Build image
docker build -t whatsapp-chatbot:latest .

# Start with docker-compose
docker-compose up -d

# Stop container
docker-compose down

# View logs (live)
docker logs -f whatsapp-chatbot

# Check running containers
docker ps

# Rebuild after code changes
docker-compose up --build -d

# Open a shell inside the container
docker exec -it whatsapp-chatbot sh

# Remove image
docker rmi whatsapp-chatbot:latest

# Remove everything (container + image + network)
docker-compose down --rmi all
```

---

### Dockerfile overview

This project uses a **2-stage Docker build** to keep the final image small:

```
Stage 1 (builder):  maven:3.9.6-eclipse-temurin-17
  → Downloads dependencies
  → Compiles source code
  → Produces whatsapp-chatbot-0.0.1-SNAPSHOT.jar

Stage 2 (runtime):  eclipse-temurin:17-jre-alpine
  → Copies only the .jar from Stage 1
  → Exposes port 8080
  → Runs the jar
  → Final image size: ~200MB (vs ~700MB single-stage)
```

---

## 📦 Push to Docker Hub

```bash
# Login to Docker Hub
docker login

# Tag the image with your Docker Hub username
docker tag whatsapp-chatbot nimeshshahi/whatsapp-chatbot:latest

# Push to Docker Hub
docker push nimeshshahi/whatsapp-chatbot:latest
```

Anyone can now run your bot with a single command:
```bash
docker run -d -p 8080:8080 nimeshshahi/whatsapp-chatbot:latest
```

---

## 📝 Console Logging Sample

Every message is logged to the console in structured format:

```
2026-03-25 10:00:00 INFO  WebhookController - 🔔 Webhook triggered — POST /webhook
2026-03-25 10:00:00 INFO  ChatbotService    - 📩 INCOMING MESSAGE | From: +919876543210 | MessageId: msg_001 | Text: "Hi" | Timestamp: 2026-03-25T10:00:00
2026-03-25 10:00:00 INFO  ChatbotService    - 📤 OUTGOING MESSAGE | To: +919876543210 | Reply: "Hello! 👋 Welcome! How can I help you today?"
2026-03-25 10:00:00 INFO  WebhookController - ✅ Message processed successfully for: +919876543210
```

To view container logs live:
```bash
docker logs -f whatsapp-chatbot
```

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

