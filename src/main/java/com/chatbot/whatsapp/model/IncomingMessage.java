package com.chatbot.whatsapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an incoming WhatsApp-style message payload.
 *
 * Example JSON:
 * {
 *   "from": "+91XXXXXXXXXX",
 *   "message": "Hi",
 *   "messageId": "msg_001",
 *   "timestamp": "2026-03-25T10:00:00Z"
 * }
 */
public class IncomingMessage {

    @JsonProperty("from")
    private String from;

    @JsonProperty("message")
    private String message;

    @JsonProperty("messageId")
    private String messageId;

    @JsonProperty("timestamp")
    private String timestamp;

    // ---- Constructors ----

    public IncomingMessage() {}

    public IncomingMessage(String from, String message, String messageId, String timestamp) {
        this.from = from;
        this.message = message;
        this.messageId = messageId;
        this.timestamp = timestamp;
    }

    // ---- Getters & Setters ----

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "IncomingMessage{" +
                "from='" + from + '\'' +
                ", message='" + message + '\'' +
                ", messageId='" + messageId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
