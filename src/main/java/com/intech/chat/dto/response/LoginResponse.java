package com.intech.chat.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors (chain = true)
public class LoginResponse {

    private String username;
    private String token;
}
