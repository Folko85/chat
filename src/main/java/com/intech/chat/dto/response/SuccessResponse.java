package com.intech.chat.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SuccessResponse {

    private String message;
}
