package com.intech.chat.service;

import com.intech.chat.dto.request.LoginRequest;
import com.intech.chat.dto.request.RegisterRequest;
import com.intech.chat.dto.request.socketio.AuthRequest;
import com.intech.chat.dto.response.LoginResponse;
import com.intech.chat.dto.response.ProfileResponse;
import com.intech.chat.dto.response.SuccessResponse;
import com.intech.chat.exception.UserExistException;
import com.intech.chat.model.User;
import com.intech.chat.model.enums.Role;
import com.intech.chat.repository.SessionRepository;
import com.intech.chat.repository.UserRepository;
import com.intech.chat.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    private final SocketService socketService;

    private final SessionRepository sessionRepository;

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Нет такого пользователя"));
        String token;
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            token = jwtProvider.generateToken(loginRequest.getUsername());
        } else throw new UsernameNotFoundException("Неверный пароль");

        return new LoginResponse().setUsername(user.getUsername()).setToken(token);

    }

    public SuccessResponse logout() {
        SecurityContextHolder.clearContext();
        return new SuccessResponse().setMessage("Вы вышли из чата");
    }

    public SuccessResponse register(RegisterRequest registerRequest) throws UserExistException {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent())
            throw new UserExistException("Пользователь с таким никнеймом уже есть");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        User user = new User().setFirstname(registerRequest.getFirstname()).setLastname(registerRequest.getLastname())
                .setUsername(registerRequest.getUsername()).setPassword(passwordEncoder.encode(registerRequest.getPassword()))
                .setRole(Role.USER);
        userRepository.save(user);
        return new SuccessResponse().setMessage("Вы успешно зарегистрированы");
    }

    public ProfileResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Нет такого пользователя"));
        return new ProfileResponse().setFirstname(user.getFirstname())
                .setLastName(user.getLastname())
                .setUsername(user.getUsername());
    }

    public void socketAuth(AuthRequest data, UUID sessionId) {
        String token = data.getToken();
        if (token != null) {
            Optional<User> person = userRepository.findByUsername(jwtProvider.getLoginFromToken(token));
            if (person.isPresent()) {
                sessionRepository.save(person.get().getId(), sessionId);
                socketService.sendEvent("auth-response", "ok", person.get().getId());
                log.info("User authorize on socket {} count {}", person.get().getUsername(), sessionRepository.findAll().size());
            }
        }
    }
}
