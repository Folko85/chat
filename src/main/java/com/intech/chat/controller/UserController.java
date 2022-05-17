package com.intech.chat.controller;

import com.intech.chat.dto.request.LoginRequest;
import com.intech.chat.dto.request.RegisterRequest;
import com.intech.chat.dto.response.LoginResponse;
import com.intech.chat.dto.response.ProfileResponse;
import com.intech.chat.dto.response.SuccessResponse;
import com.intech.chat.exception.IncorrectNiknameException;
import com.intech.chat.exception.UserExistException;
import com.intech.chat.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "Контроллер для авторизации")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Регистрация")
    public SuccessResponse register(@RequestBody RegisterRequest registerRequest) throws UserExistException, IncorrectNiknameException {
        return userService.register(registerRequest);
    }

    @PostMapping("/login")
    @Operation(summary = "Вход")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) throws Exception {
        return userService.login(loginRequest);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Данные пользователя", security = @SecurityRequirement(name = "jwt"))
    public ProfileResponse getProfile(@PathVariable String username) {
        return userService.getProfile(username);
    }

    @GetMapping("/logout")
    @Operation(summary = "Выход")
    public SuccessResponse logout() {
        return userService.logout();
    }
}
