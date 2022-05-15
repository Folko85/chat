package com.intech.chat.dto.response.socketio;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class TypingResponse {
    private long authorId;
    private String author;
    private int dialog;
}
