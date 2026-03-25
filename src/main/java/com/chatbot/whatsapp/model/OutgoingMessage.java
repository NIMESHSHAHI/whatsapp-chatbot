package com.chatbot.whatsapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the chatbot's reply to an incoming message.
 *
 * Example JSON Response:
 * {
 *   "to": "+91XXXXXXXXXX",
 *   "reply": "Hello! How can I help you?",
 *   "status": "sent",
 *   "originalMessage": "Hi",
 *   "processedAt": "2026-03-25T10:00:01Z"
 * }
 */
public class OutgoingMessage {

    @JsonProperty("to")
    private String to;

    @JsonProperty("reply")
    private String reply;

    @JsonProperty("status")
    private String status;

    @JsonProperty("originalMessage")
    private String originalMessage;

    @JsonProperty("processedAt")
    private String processedAt;

    // ---- Constructors ----

    public OutgoingMessage() {}

    public OutgoingMessage(String to, String reply, String status, String originalMessage, String processedAt) {
        this.to = to;
        this.reply = reply;
        this.status = status;
        this.originalMessage = originalMessage;
        this.processedAt = processedAt;
    }

    // ---- Getters & Setters ----

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOriginalMessage() { return originalMessage; }
    public void setOriginalMessage(String originalMessage) { this.originalMessage = originalMessage; }

    public String getProcessedAt() { return processedAt; }
    public void setProcessedAt(String processedAt) { this.processedAt = processedAt; }

    @Override
    public String toString() {
        return "OutgoingMessage{" +
                "to='" + to + '\'' +
                ", reply='" + reply + '\'' +
                ", status='" + status + '\'' +
                ", originalMessage='" + originalMessage + '\'' +
                ", processedAt='" + processedAt + '\'' +
                '}';
    }
}
