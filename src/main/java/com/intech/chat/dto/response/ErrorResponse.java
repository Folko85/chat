package com.intech.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors (chain = true)
public class ErrorResponse {
    private String error;
    @JsonProperty("error_description")
    private String errorDescription;
}