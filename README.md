# 🤖 WhatsApp Chatbot Backend Simulation
### Built with Java 17 + Spring Boot 3 | Internship Assignment

---

## 📌 Project Overview

A **REST API backend** that simulates a WhatsApp chatbot webhook.  
It accepts POST requests with JSON payloads (simulating incoming WhatsApp messages) and responds with predefined smart replies — just like the WhatsApp Business API works.

---

## 🏗️ Project Structure

```
whatsapp-chatbot/
├── src/
│   ├── main/
│   │   ├── java/com/chatbot/whatsapp/
│   │   │   ├── WhatsappChatbotApplication.java   ← Entry point
│   │   │   ├── controller/
│   │   │   │   └── WebhookController.java         ← REST endpoints
│   │   │   ├── service/
│   │   │   │   └── ChatbotService.java             ← Reply logic + logging
│   │   │   └── model/
│   │   │       ├── IncomingMessage.java            ← Request model
│   │   │       └── OutgoingMessage.java            ← Response model
│   │   └── resources/
│   │       └── application.properties             ← App config
│   └── test/
│       └── java/com/chatbot/whatsapp/
│           └── WhatsappChatbotApplicationTests.java ← Unit + Integration tests
├── pom.xml                                         ← Maven dependencies
├── render.yaml                                     ← Render.com deploy config
└── README.md
```

---

## ⚙️ Prerequisites

| Tool        | Version     |
|-------------|-------------|
| Java JDK    | 17 or above |
| Maven       | 3.8+        |
| Git         | Any         |
| Postman     | (for testing) |

---

## 🚀 Local Setup & Run

### Step 1 — Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/whatsapp-chatbot.git
cd whatsapp-chatbot
```

### Step 2 — Build the project
```bash
./mvnw clean package -DskipTests
```

### Step 3 — Run the application
```bash
./mvnw spring-boot:run
```

The server starts at: **http://localhost:8080**

---

## 📡 API Endpoints

### 1. `POST /webhook` — Send a message to the bot

**Request:**
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

### 2. `GET /webhook` — Webhook Verification (Meta-style)

```
GET /webhook?hub.mode=subscribe&hub.verify_token=whatsapp_bot_token_2026&hub.challenge=your_challenge
```
Returns `200 OK` with the challenge echoed back.

---

### 3. `GET /health` — Health Check

```
GET /health
```
```json
{
  "status": "UP",
  "service": "WhatsApp Chatbot Backend",
  "version": "1.0.0"
}
```

---

## 💬 Supported Bot Commands

| User sends       | Bot replies                                      |
|------------------|--------------------------------------------------|
| `Hi`             | Hello! 👋 Welcome! How can I help you today?    |
| `Hello`          | Hey there! 😊 How can I assist you?             |
| `Bye`            | Goodbye! 👋 Have a great day!                   |
| `Goodbye`        | Take care! See you soon. 😊                     |
| `Help`           | Instructions on available commands              |
| `Thanks`         | You're welcome! 😊                              |
| `Thank you`      | You're most welcome! 🙌                         |
| `How are you`    | I'm just a bot, but doing great! 🤖             |
| `Services`       | Lists available services (1, 2, 3)              |
| `1` / `2` / `3`  | Service-specific info                           |
| *(anything else)*| Default fallback reply                          |

> ℹ️ All message matching is **case-insensitive**.

---

## 🧪 Testing with cURL

```bash
# Test Hi
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"from":"+919876543210","message":"Hi","messageId":"msg_001","timestamp":"2026-03-25T10:00:00"}'

# Test Bye
curl -X POST http://localhost:8080/webhook \
  -H "Content-Type: application/json" \
  -d '{"from":"+919876543210","message":"Bye","messageId":"msg_002","timestamp":"2026-03-25T10:01:00"}'

# Health check
curl http://localhost:8080/health
```

---

## 🧪 Run Unit Tests

```bash
./mvnw test
```

Tests cover:
- ✅ Hi → Hello reply
- ✅ Bye → Goodbye reply
- ✅ Unknown message → Default fallback
- ✅ Empty message handling
- ✅ Case-insensitivity
- ✅ POST /webhook HTTP integration test
- ✅ GET /webhook verification (valid + invalid token)
- ✅ GET /health endpoint

---

## ☁️ Deploy on Render (Bonus)

1. Push this repo to GitHub
2. Go to [render.com](https://render.com) → **New Web Service**
3. Connect your GitHub repository
4. Render auto-detects `render.yaml` — click **Deploy**
5. Your bot will be live at: `https://whatsapp-chatbot.onrender.com`

---

## 📝 Console Logging Sample

```
2026-03-25 10:00:00 [http-nio-8080-exec-1] INFO  WebhookController - 🔔 Webhook triggered — POST /webhook
2026-03-25 10:00:00 [http-nio-8080-exec-1] INFO  ChatbotService    - 📩 INCOMING MESSAGE | From: +919876543210 | MessageId: msg_001 | Text: "Hi" | Timestamp: 2026-03-25T10:00:00
2026-03-25 10:00:00 [http-nio-8080-exec-1] INFO  ChatbotService    - 📤 OUTGOING MESSAGE | To: +919876543210 | Reply: "Hello! 👋 Welcome! How can I help you today?"
2026-03-25 10:00:00 [http-nio-8080-exec-1] INFO  WebhookController - ✅ Message processed successfully for: +919876543210
```

---

## 🛠️ Tech Stack

| Technology        | Purpose                    |
|-------------------|----------------------------|
| Java 17           | Core language              |
| Spring Boot 3.2   | REST API framework         |
| Spring Web MVC    | Controller / routing       |
| Spring Actuator   | Health check endpoints     |
| SLF4J + Logback   | Message logging            |
| JUnit 5 + MockMvc | Unit & integration tests   |
| Maven             | Build tool                 |
| Render.com        | Free cloud hosting (Bonus) |

---

## 👤 Author

Built as part of an internship assignment submission.  
Deadline: **27 March 2026**
