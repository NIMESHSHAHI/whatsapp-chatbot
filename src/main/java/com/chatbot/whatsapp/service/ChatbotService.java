package com.chatbot.whatsapp.service;

import com.chatbot.whatsapp.model.IncomingMessage;
import com.chatbot.whatsapp.model.OutgoingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Core chatbot service.
 * Handles message processing, predefined reply lookup, and logging.
 */
@Service
public class ChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotService.class);

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Predefined reply map (case-insensitive keyword → reply)
     * Easily extensible — just add more entries here.
     */
    private final Map<String, String> replyMap = new HashMap<>() {{
        put("hi",       "Hello! 👋 Welcome! How can I help you today?");
        put("hello",    "Hey there! 😊 How can I assist you?");
        put("bye",      "Goodbye! 👋 Have a great day!");
        put("goodbye",  "Take care! See you soon. 😊");
        put("help",     "Sure! I can assist you. Type 'Hi' to start or 'Bye' to end the conversation.");
        put("thanks",   "You're welcome! 😊 Is there anything else I can help you with?");
        put("thank you","You're most welcome! 🙌 Feel free to ask anything.");
        put("how are you", "I'm just a bot, but I'm doing great! 🤖 How about you?");
        put("what is your name", "I'm WhatsApp Bot, your virtual assistant! 🤖");
        put("services", "We offer: \n1️⃣ Customer Support\n2️⃣ Product Info\n3️⃣ Order Tracking\nType a number to know more!");
        put("1",        "📞 Customer Support: Our team is available 24/7 to assist you!");
        put("2",        "📦 Product Info: We have a wide range of products. Visit our website for details.");
        put("3",        "🚚 Order Tracking: Please share your order ID to track your package.");
    }};

    /**
     * Default fallback reply
     */
    private static final String DEFAULT_REPLY =
            "I'm sorry, I didn't understand that. 🤔\nType 'Hi' to start or 'Help' for available commands.";

    /**
     * Processes an incoming message and returns a bot reply.
     *
     * @param incoming The incoming WhatsApp message
     * @return OutgoingMessage with the chatbot reply
     */
    public OutgoingMessage processMessage(IncomingMessage incoming) {

        // --- Log the incoming message ---
        logger.info("📩 INCOMING MESSAGE | From: {} | MessageId: {} | Text: \"{}\" | Timestamp: {}",
                incoming.getFrom(),
                incoming.getMessageId(),
                incoming.getMessage(),
                incoming.getTimestamp());

        // --- Validate input ---
        if (incoming.getMessage() == null || incoming.getMessage().isBlank()) {
            logger.warn("⚠️  Empty message received from: {}", incoming.getFrom());
            OutgoingMessage errorResponse = new OutgoingMessage(
                    incoming.getFrom(),
                    "Please send a valid message. 📝",
                    "sent",
                    incoming.getMessage(),
                    getCurrentTimestamp()
            );
            logger.info("📤 OUTGOING MESSAGE | To: {} | Reply: \"{}\"",
                    errorResponse.getTo(), errorResponse.getReply());
            return errorResponse;
        }

        // --- Match reply (case-insensitive, trimmed) ---
        String userText = incoming.getMessage().trim().toLowerCase();
        String reply = replyMap.getOrDefault(userText, DEFAULT_REPLY);

        // --- Build response ---
        OutgoingMessage response = new OutgoingMessage(
                incoming.getFrom(),
                reply,
                "sent",
                incoming.getMessage(),
                getCurrentTimestamp()
        );

        // --- Log the outgoing reply ---
        logger.info("📤 OUTGOING MESSAGE | To: {} | Reply: \"{}\"",
                response.getTo(), response.getReply());

        return response;
    }

    /**
     * Returns current timestamp as a formatted string.
     */
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(FORMATTER);
    }
}
