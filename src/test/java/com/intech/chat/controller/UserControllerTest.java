package com.intech.chat.controller;

import com.intech.chat.AbstractTest;
import com.intech.chat.ChatApplication;
import com.intech.chat.dto.request.LoginRequest;
import com.intech.chat.model.User;
import com.intech.chat.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = {ChatApplication.class})
@AutoConfigureMockMvc
public class UserControllerTest extends AbstractTest {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();

    }

    @Test
    @DisplayName("Успешный вход в аккаунт c логином и паролем")
    void testLogin() throws Exception {
        LoginRequest request = new LoginRequest().setUsername("test").setPassword("password");
        User user = new User().setUsername("test").setPassword(passwordEncoder.encode("password"))
                .setFirstname("Name").setLastname("LastName");
        userRepository.save(user);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Попытка входа несуществующего пользователя")
    void testNotLogin() throws Exception {
        LoginRequest request = new LoginRequest().setUsername("none").setPassword("password");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
