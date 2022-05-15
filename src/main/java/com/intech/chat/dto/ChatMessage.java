package com.intech.chat.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;
    private LocalDateTime dateTime;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

}