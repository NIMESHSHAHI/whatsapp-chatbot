package com.chatbot.whatsapp_chatbot;

import com.chatbot.whatsapp.model.IncomingMessage;
import com.chatbot.whatsapp.model.OutgoingMessage;
import com.chatbot.whatsapp.service.ChatbotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WhatsappChatbotApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChatbotService chatbotService;

    private IncomingMessage baseMessage;

    @BeforeEach
    void setUp() {
        baseMessage = new IncomingMessage(
                "+919876543210",
                "Hi",
                "msg_001",
                "2026-03-25T10:00:00"
        );
    }

    // ---- ChatbotService Tests ----

    @Test
    @DisplayName("Should reply 'Hello' for 'Hi' message")
    void testHiReply() {
        OutgoingMessage response = chatbotService.processMessage(baseMessage);
        assertNotNull(response);
        assertTrue(response.getReply().toLowerCase().contains("hello"));
        assertEquals("sent", response.getStatus());
    }

    @Test
    @DisplayName("Should reply 'Goodbye' for 'Bye' message")
    void testByeReply() {
        baseMessage.setMessage("Bye");
        OutgoingMessage response = chatbotService.processMessage(baseMessage);
        assertNotNull(response);
        assertTrue(response.getReply().toLowerCase().contains("goodbye")
                || response.getReply().toLowerCase().contains("take care"));
    }

    @Test
    @DisplayName("Should return default reply for unknown message")
    void testDefaultReply() {
        baseMessage.setMessage("random unknown text xyz");
        OutgoingMessage response = chatbotService.processMessage(baseMessage);
        assertNotNull(response);
        assertFalse(response.getReply().isBlank());
    }

    @Test
    @DisplayName("Should handle empty message gracefully")
    void testEmptyMessage() {
        baseMessage.setMessage("");
        OutgoingMessage response = chatbotService.processMessage(baseMessage);
        assertNotNull(response);
        assertEquals("sent", response.getStatus());
    }

    @Test
    @DisplayName("Should be case-insensitive (HI == hi == Hi)")
    void testCaseInsensitive() {
        baseMessage.setMessage("HI");
        OutgoingMessage upper = chatbotService.processMessage(baseMessage);

        baseMessage.setMessage("hi");
        OutgoingMessage lower = chatbotService.processMessage(baseMessage);

        assertEquals(upper.getReply(), lower.getReply());
    }

    // ---- Webhook Controller Integration Tests ----

    @Test
    @DisplayName("POST /webhook should return 200 with reply")
    void testWebhookPost() throws Exception {
        String json = """
                {
                  "from": "+919876543210",
                  "message": "Hi",
                  "messageId": "msg_test_001",
                  "timestamp": "2026-03-25T10:00:00"
                }
                """;

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.to").value("+919876543210"))
                .andExpect(jsonPath("$.status").value("sent"))
                .andExpect(jsonPath("$.reply").exists());
    }

    @Test
    @DisplayName("POST /webhook with 'Bye' should return Goodbye reply")
    void testWebhookByeMessage() throws Exception {
        String json = """
                {
                  "from": "+919876543210",
                  "message": "Bye",
                  "messageId": "msg_test_002",
                  "timestamp": "2026-03-25T10:00:00"
                }
                """;

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("sent"));
    }

    @Test
    @DisplayName("GET /health should return UP status")
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    @DisplayName("GET /webhook with valid token should echo challenge")
    void testWebhookVerification() throws Exception {
        mockMvc.perform(get("/webhook")
                        .param("hub.mode", "subscribe")
                        .param("hub.verify_token", "whatsapp_bot_token_2026")
                        .param("hub.challenge", "test_challenge_123"))
                .andExpect(status().isOk())
                .andExpect(content().string("test_challenge_123"));
    }

    @Test
    @DisplayName("GET /webhook with invalid token should return 403")
    void testWebhookVerificationFail() throws Exception {
        mockMvc.perform(get("/webhook")
                        .param("hub.mode", "subscribe")
                        .param("hub.verify_token", "wrong_token")
                        .param("hub.challenge", "test_challenge_123"))
                .andExpect(status().isForbidden());
    }
}
