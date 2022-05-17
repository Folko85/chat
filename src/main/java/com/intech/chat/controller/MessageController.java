package com.intech.chat.controller;

import com.intech.chat.dto.response.MessageResponse;
import com.intech.chat.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/messages")
@Tag(name = "Контроллер работы с сообщениями")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Получить все сообщения", security = @SecurityRequirement(name = "jwt"))
    public List<MessageResponse> getAll() {
        return messageService.getAll();
    }

}
