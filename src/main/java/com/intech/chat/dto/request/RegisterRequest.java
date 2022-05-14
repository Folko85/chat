package com.intech.chat.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String username;
    private String password;
}
