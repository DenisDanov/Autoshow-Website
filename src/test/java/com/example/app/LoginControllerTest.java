package com.example.app;

import com.example.app.data.entities.FailedLoginAttempts;
import com.example.app.services.FailedLoginAttemptsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Calendar;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginControllerTest {

    private static final String SECRET_KEY = "312op3v53452jv231VWQE3v167456psdapqovk12opvj2opevjas312jvd1jQWEQWE1V21dsa41241vsada123v41AS23vj1p23v123v1kdpaWEQE12VGGDFKPOGKDPFOs";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FailedLoginAttemptsService failedLoginAttemptsService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testIncorrectCredentials() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "denko3v1")
                        .param("password", "wrongPassword"))
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    public void testAccountLockoutWithInvalidUsername() throws Exception {
        Calendar calendar = Calendar.getInstance();

        // Add 30 minutes
        calendar.add(Calendar.MINUTE, 30);

        // Get the date after adding 30 minutes
        Date currentDatePlus30Minutes = calendar.getTime();
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .cookie(new Cookie("account_lock", generateTokenFailedLogins
                                (currentDatePlus30Minutes, null, 10, null))) // set the cookie in the request
                        .param("username", "denkov21341")
                        .param("password", "wrongPassword"))
                .andExpect(redirectedUrl("/login?errorTooManyFailedAttempts"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Order(1)
    public void testAccountLockoutWithValidUsername() throws Exception {
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

    @Test
    @Order(2)
    public void testLoggingWithCorrectCredentialsButWithLockedAcc() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mockMvc.perform(post("/login")
                        .param("username", "denko")
                        .param("password", "denko1234"))
                .andExpect(redirectedUrl("/login?errorTooManyFailedAttempts"));
        failedLoginAttemptsService.deleteByUser_Username("denko");
    }

    @Test
    @Order(3)
    public void testLoggingInWithValidCredentialsAndNoLockedAccount() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mockMvc.perform(post("/login")
                        .param("username", "denko")
                        .param("password", "denko1234"))
                .andExpect(redirectedUrl("/index"));
    }

    private static String generateTokenFailedLogins(Date expirationDate, Long userId, int failedTimes,
                                                    FailedLoginAttempts failedLoginAttempts) {
        boolean isUserLocked = false;
        if (failedLoginAttempts != null) {
            isUserLocked = failedLoginAttempts.isUserLocked();
        }
        if (failedTimes >= 10) {
            isUserLocked = true;
        }
        if (expirationDate != null) {
            return Jwts.builder()
                    .setExpiration(expirationDate)
                    .claim("expirationDate", expirationDate.getTime())
                    .claim("id", userId)
                    .claim("failed_logins_amount", failedTimes)
                    .claim("isUserLocked", isUserLocked)
                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                    .compact();
        } else {
            return Jwts.builder()
                    .setExpiration(expirationDate)
                    .claim("expirationDate", 0)
                    .claim("id", userId)
                    .claim("failed_logins_amount", failedTimes)
                    .claim("isUserLocked", isUserLocked)
                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                    .compact();
        }
    }
}
