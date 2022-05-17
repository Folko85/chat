package com.intech.chat.service;

import com.intech.chat.dto.ChatMessage;
import com.intech.chat.model.Message;
import com.intech.chat.model.User;
import com.intech.chat.repository.MessageRepository;
import com.intech.chat.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public List<ChatMessage> getLast() {
        List<Message> messages = messageRepository.findAllByDateTimeAfter(LocalDateTime.now().minusHours(1));
        return messages.stream().map(m -> new ChatMessage().setSender(m.getUser().getUsername()).setContent(m.getText())
                .setDateTime(m.getDateTime()).setType(ChatMessage.MessageType.CHAT)).collect(Collectors.toList());
    }

    public ChatMessage addMessage(ChatMessage chatMessage) {
        log.info(String.valueOf(chatMessage));
        User user = userRepository.findByUsername(chatMessage.getSender())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        messageRepository.save(new Message().setUser(user).setText(chatMessage.getContent()).setDateTime(chatMessage.getDateTime()));
        return chatMessage;
    }
}
