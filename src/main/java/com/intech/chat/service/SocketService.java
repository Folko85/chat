package com.intech.chat.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.intech.chat.dto.request.socketio.ReadMessagesData;
import com.intech.chat.dto.request.socketio.TypingData;
import com.intech.chat.dto.response.socketio.TypingResponse;
import com.intech.chat.repository.SessionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SocketService {
    private final SocketIOServer server;
    private final SessionRepository sessionRepository;

    public void sendEvent(String eventName, TypingResponse data, long personId) {
        sessionRepository.findByUserId(personId).ifPresent(uuid -> server.getClient(uuid).sendEvent(eventName, data));
        log.info("send event {} to {}", eventName, personId);
    }

    public void sendEvent(String eventName, TypingData data, long personId) {
        sessionRepository.findByUserId(personId).ifPresent(uuid -> server.getClient(uuid).sendEvent(eventName, data));
        log.info("send event {} to {}", eventName, personId);
    }

    public void sendEvent(String eventName, ReadMessagesData data, long personId) {
        sessionRepository.findByUserId(personId).ifPresent(uuid -> server.getClient(uuid).sendEvent(eventName, data));
        log.info("send event {} to {}", eventName, personId);
    }

    public void sendEvent(String eventName, String data, long personId) {
        sessionRepository.findByUserId(personId).ifPresent(uuid -> server.getClient(uuid).sendEvent(eventName, data));
        log.info("send event {} to {}", eventName, personId);
    }
}
