package com.chatbot.whatsapp.controller;

import com.chatbot.whatsapp.model.IncomingMessage;
import com.chatbot.whatsapp.model.OutgoingMessage;
import com.chatbot.whatsapp.service.ChatbotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Webhook Controller — simulates a WhatsApp Business API webhook.
 *
 * Endpoints:
 *  POST /webhook  → Receives and processes incoming WhatsApp messages
 *  GET  /webhook  → Webhook verification (simulating Meta's hub challenge)
 *  GET  /health   → Health check
 */
@RestController
@RequestMapping("/")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    private final ChatbotService chatbotService;

    // Webhook verification token (for simulation purposes)
    private static final String VERIFY_TOKEN = "whatsapp_bot_token_2026";

    public WebhookController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    // =========================================================================
    //  POST /webhook — Receive & process incoming messages
    // =========================================================================

    /**
     * Accepts incoming WhatsApp message payloads and returns a bot reply.
     *
     * Sample Request Body:
     * {
     *   "from": "+91XXXXXXXXXX",
     *   "message": "Hi",
     *   "messageId": "msg_001",
     *   "timestamp": "2026-03-25T10:00:00"
     * }
     */
    @PostMapping(
        value = "/webhook",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<OutgoingMessage> receiveMessage(@RequestBody IncomingMessage incoming) {

        logger.info("🔔 Webhook triggered — POST /webhook");

        // Basic validation
        if (incoming == null || incoming.getFrom() == null) {
            logger.error("❌ Invalid request: missing required fields");
            return ResponseEntity.badRequest().build();
        }

        // Process message through chatbot service
        OutgoingMessage response = chatbotService.processMessage(incoming);

        logger.info("✅ Message processed successfully for: {}", incoming.getFrom());

        return ResponseEntity.ok(response);
    }

    // =========================================================================
    //  GET /webhook — Webhook verification (Meta WhatsApp Business API style)
    // =========================================================================

    /**
     * Simulates the Meta webhook verification handshake.
     * In real WhatsApp API: hub.challenge is echoed back to confirm the endpoint.
     */
    @GetMapping("/webhook")
    public ResponseEntity<String> verifyWebhook(
            @RequestParam(value = "hub.mode",      required = false) String mode,
            @RequestParam(value = "hub.verify_token", required = false) String token,
            @RequestParam(value = "hub.challenge", required = false) String challenge) {

        logger.info("🔐 Webhook verification attempt | Mode: {} | Token: {}", mode, token);

        if ("subscribe".equals(mode) && VERIFY_TOKEN.equals(token)) {
            logger.info("✅ Webhook verified successfully!");
            return ResponseEntity.ok(challenge);
        }

        logger.warn("❌ Webhook verification FAILED — invalid token or mode");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Verification failed");
    }

    // =========================================================================
    //  GET /health — Health check endpoint
    // =========================================================================

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status",  "UP");
        health.put("service", "WhatsApp Chatbot Backend");
        health.put("version", "1.0.0");
        health.put("message", "Bot is running and ready to receive messages 🤖");
        return ResponseEntity.ok(health);
    }

    // =========================================================================
    //  GET / — Root endpoint with usage info
    // =========================================================================

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> info = new HashMap<>();
        info.put("app",         "WhatsApp Chatbot Backend Simulation");
        info.put("version",     "1.0.0");
        info.put("author",      "Internship Assignment");
        info.put("status",      "Running ✅");
        info.put("endpoints",   new String[]{
                "POST /webhook  — Send a message to the bot",
                "GET  /webhook  — Webhook verification",
                "GET  /health   — Health check"
        });
        info.put("samplePayload", Map.of(
                "from",      "+91XXXXXXXXXX",
                "message",   "Hi",
                "messageId", "msg_001",
                "timestamp", "2026-03-25T10:00:00"
        ));
        return ResponseEntity.ok(info);
    }
}
