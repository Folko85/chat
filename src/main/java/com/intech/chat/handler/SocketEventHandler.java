package com.intech.chat.handler;


import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.intech.chat.dto.request.socketio.AuthRequest;
import com.intech.chat.dto.request.socketio.ReadMessagesData;
import com.intech.chat.dto.request.socketio.TypingData;
import com.intech.chat.repository.SessionRepository;
import com.intech.chat.service.MessageService;
import com.intech.chat.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class SocketEventHandler {
    private final SocketIOServer server;
    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final MessageService messageService;


    /**
     * Ивент "рукопожатия"
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("User connect on socket user {} count {}", client.getSessionId(), (long) server.getAllClients().size());
    }

    /**
     * Ивент отключения
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        if (client != null) {
            Optional<Long> id = sessionRepository.findByUserUUID(client.getSessionId());
            if (id.isPresent()) {
                sessionRepository.deleteByUserId(id.get());
                client.disconnect();
                log.info("User disconnect on socket count {}", (long) server.getAllClients().size());
            }
        }
    }

    /**
     * Ивент для проверки авторизации
     */
    @OnEvent(value = "newListener")
    public void onNewListenerEvent(SocketIOClient client) {
        log.info("User listen on socket");
        if (client != null) {
            if (sessionRepository.findByUserUUID(client.getSessionId()).isPresent()) {
                client.sendEvent("auth-response", "ok");
            } else client.sendEvent("auth-response", "not");
        }
    }

    /**
     * Ивент авторизации
     */
    @OnEvent(value = "auth")
    public void onAuthEvent(SocketIOClient client, AckRequest request, AuthRequest data) {
        log.info("Авторизация");
        if (client != null) {
            userService.socketAuth(data, client.getSessionId());
        }
    }

    @OnEvent(value = "start-typing")
    public void onStartTypingEvent(SocketIOClient client, AckRequest request, TypingData data) {
        if (client != null) {
            messageService.startTyping(data);
        }
    }

    @OnEvent(value = "stop-typing")
    public void onStopTypingEvent(SocketIOClient client, AckRequest request, TypingData data) {
        if (client != null) {
            sessionRepository.findByUserUUID(client.getSessionId()).ifPresent(id -> messageService.stopTyping(data));

        }
    }

    /**
     * Ивент прочтения сообщений
     */
    @OnEvent(value = "read-messages")
    public void onReadMessagesEvent(SocketIOClient client, AckRequest request, ReadMessagesData data) {
        if (client != null) {
            sessionRepository.findByUserUUID(client.getSessionId()).ifPresent(id -> messageService.readMessage(data, id));
        }
    }
}
