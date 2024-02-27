package com.example.demo;

import com.example.demo.dbData.User;
import com.example.demo.dbData.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrationControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testSuccessfulRegistration() throws Exception {

        String username = "denko3v1";
        String password = "denko3v1";
        String email = "denko3v1@abv.bg";

        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("password", password)
                        .param("email", email))
                .andExpect(redirectedUrl("https://danov-autoshow-656625355b99.herokuapp.com/login"));

        User user = new User(username,password,email);

        assertNotNull(userRepository.findByUsername(username));
        User userDB = userRepository.findByUsername("denko3v1");
        assertEquals(userDB.getEmail(),user.getEmail());
        assertEquals(userDB.getPassword(),user.getPassword());
        assertEquals(userDB.getUsername(),user.getUsername());
    }

    @Test
    public void testUserWithThisUsernameExists() throws Exception {
        String username = "denko3v1";
        String password = "denko3v1";
        String email = "denko3v1@abv.bg";

        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("password", password)
                        .param("email", email))
                .andExpect(redirectedUrl("https://danov-autoshow-656625355b99.herokuapp.com/login"));

        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("password", password)
                        .param("email", email))
                .andExpect(redirectedUrl("/register?error"));
    }

    @Test
    public void testUserWithThisEmailExists() throws Exception {
        String username = "denko3v1";
        String password = "denko3v1";
        String email = "denko3v1@abv.bg";

        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("password", password)
                        .param("email", email))
                .andExpect(redirectedUrl("https://danov-autoshow-656625355b99.herokuapp.com/login"));

        mockMvc.perform(post("/register")
                        .param("username", username + "L9")
                        .param("password", password)
                        .param("email", email))
                .andExpect(redirectedUrl("/register?errorEmailExists"));
    }

    @Test
    public void testRegistrationWithInvalidEmail() throws Exception {
        String username = "denko3v1";
        String password = "denko3v1";
        String email = "denko3v1abv.bg";

        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("password", password)
                        .param("email", email))
                .andExpect(redirectedUrl("/register?errorEmail"));
    }

    @Test
    public void testRegistrationWithInvalidPassword() throws Exception {
        String username = "denko3v1";
        String password = "denko";
        String email = "denko3v1@abv.bg";

        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("password", password)
                        .param("email", email))
                .andExpect(redirectedUrl("/register?errorPassword"));
    }
}
