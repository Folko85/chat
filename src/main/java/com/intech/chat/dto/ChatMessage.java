package com.intech.chat.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
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