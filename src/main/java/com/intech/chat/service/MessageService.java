package com.intech.chat.service;

import com.intech.chat.dto.request.MessageRequest;
import com.intech.chat.dto.request.socketio.ReadMessagesData;
import com.intech.chat.dto.request.socketio.TypingData;
import com.intech.chat.dto.response.MessageResponse;
import com.intech.chat.dto.response.SuccessResponse;
import com.intech.chat.dto.response.socketio.TypingResponse;
import com.intech.chat.model.Message;
import com.intech.chat.model.User;
import com.intech.chat.repository.MessageRepository;
import com.intech.chat.repository.SessionRepository;
import com.intech.chat.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;

    private final SocketService socketService;

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

    public void startTyping(TypingData typingData) {
        Optional<Message> dialog = messageRepository.findById(typingData.getDialog());
        Optional<User> personOptional = userRepository.findById(typingData.getAuthor());

        if (dialog.isPresent() && personOptional.isPresent()) {
            userRepository.findAll().forEach(person -> {
                if (person.getId() != typingData.getAuthor()) {
                    TypingResponse typingResponse = new TypingResponse();
                    typingResponse.setDialog(typingData.getDialog());
                    typingResponse.setAuthorId(typingData.getAuthor());
                    typingResponse.setAuthor(personOptional.get().getFirstname());
                    socketService.sendEvent("start-typing-response", typingResponse, person.getId());
                }
            });
        }
    }

    public void stopTyping(TypingData typingData) {
        Optional<Message> dialog = messageRepository.findById(typingData.getDialog());
        Optional<User> personOptional = userRepository.findById(typingData.getAuthor());
        if (dialog.isPresent() && personOptional.isPresent()) {
            userRepository.findAll().forEach(person -> {
                if (person.getId() != typingData.getAuthor())
                    socketService.sendEvent("stop-typing-response", typingData, person.getId());
            });
        }
    }

    public void readMessage(ReadMessagesData readMessagesData, long personId) {
        Optional<Message> dialog = messageRepository.findById(readMessagesData.getDialog());
        Optional<User> personOptional = userRepository.findById(personId);
        if (dialog.isPresent() && personOptional.isPresent()) {
            userRepository.findAll().forEach(person -> socketService.sendEvent("unread-response", readMessagesData, personOptional.get().getId()));
        }
    }
}
