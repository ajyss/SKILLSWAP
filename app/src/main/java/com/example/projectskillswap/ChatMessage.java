package com.example.projectskillswap;

public class ChatMessage {
    private String message;
    private boolean isMe;

    public ChatMessage(String message, boolean isMe) {
        this.message = message;
        this.isMe = isMe;
    }

    public String getMessage() { return message; }
    public boolean isMe() { return isMe; }
}
