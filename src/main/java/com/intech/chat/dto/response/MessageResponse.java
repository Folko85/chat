package com.intech.chat.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class MessageResponse {

    private String username;
    private LocalDateTime dateTime;
    private String text;
}
