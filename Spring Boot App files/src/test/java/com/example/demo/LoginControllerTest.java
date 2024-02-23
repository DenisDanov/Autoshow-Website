package com.example.demo;

import com.example.demo.dbData.LoginController;
import com.example.demo.dbData.UserRepository;
import com.example.demo.dbData.unsuccessfulLoginAttempts.FailedLoginAttemptsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FailedLoginAttemptsRepository failedLoginAttemptsRepository;

    @InjectMocks
    private LoginController loginController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(loginController).build();
    }

    @Test
    public void testIncorrectCredentials() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "denko3v1")
                        .param("password", "wrongPassword"))
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    public void testAccountLockout() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        for (int i = 1; i <= 9; i++) {
            mockMvc.perform(post("/login")
                            .param("username", "denko")
                            .param("password", "wrongPassword"))
                    .andExpect(redirectedUrl("/login?error"));
        }
        mockMvc.perform(post("/login")
                        .param("username", "denko")
                        .param("password", "wrongPassword"))
                .andExpect(redirectedUrl("/login?errorTooManyFailedAttempts"));
    }

}
