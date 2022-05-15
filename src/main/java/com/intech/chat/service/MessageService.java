package com.intech.chat.service;

import com.intech.chat.dto.ChatMessage;
import com.intech.chat.dto.request.MessageRequest;
import com.intech.chat.dto.response.MessageResponse;
import com.intech.chat.dto.response.SuccessResponse;
import com.intech.chat.model.Message;
import com.intech.chat.model.User;
import com.intech.chat.repository.MessageRepository;
import com.intech.chat.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public List<MessageResponse> getAll() {
        List<Message> messages = messageRepository.findAll();
        return messages.stream().map(m -> new MessageResponse().setUsername(m.getUser().getUsername()).setText(m.getText())
                .setDateTime(m.getDateTime())).collect(Collectors.toList());
    }

    public SuccessResponse add(MessageRequest messageRequest, Principal principal) {
        log.info(principal.toString());
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        messageRepository.save(new Message().setUser(user).setText(messageRequest.getMessage()).setDateTime(LocalDateTime.now()));
        return new SuccessResponse().setMessage("Сообщение отправлено");
    }

    public ChatMessage addMessage(ChatMessage chatMessage) {
        log.info(String.valueOf(chatMessage));
        User user = userRepository.findByUsername(chatMessage.getSender())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        messageRepository.save(new Message().setUser(user).setText(chatMessage.getContent()).setDateTime(chatMessage.getDateTime()));
        return chatMessage;
    }
}
