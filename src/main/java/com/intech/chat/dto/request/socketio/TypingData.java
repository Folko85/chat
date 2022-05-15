package com.intech.chat.dto.request.socketio;

import lombok.Data;

@Data
public class TypingData {
    private long author;
    private int dialog;
}
