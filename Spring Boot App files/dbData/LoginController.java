package com.example.demo.dbData;

import com.example.demo.dbData.recentlyViewedToken.RecentlyViewedRepository;
import com.example.demo.dbData.recentlyViewedToken.RecentlyViewedToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

import static java.time.temporal.ChronoUnit.SECONDS;

@Controller
public class LoginController {
    private final String SECRET_KEY = generateRandomKey();

    private static String generateRandomKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[64]; // Token length

        secureRandom.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    private final UserRepository userRepository;

    private final RecentlyViewedRepository recentlyViewedRepository;

    private final AuthenticationTokensRepository authenticationTokensRepository;

    @Autowired
    public LoginController(UserRepository userRepository,
                           RecentlyViewedRepository recentlyViewedRepository,
                           AuthenticationTokensRepository authenticationTokensRepository) {
        this.userRepository = userRepository;
        this.recentlyViewedRepository = recentlyViewedRepository;
        this.authenticationTokensRepository = authenticationTokensRepository;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginUser", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("loginUser") User loginUser,
                               HttpServletResponse response) throws UnsupportedEncodingException {
        // Retrieve the user from the database based on the entered username
        User userFromDB = userRepository.findByUsername(loginUser.getUsername());

        if (userFromDB != null && userFromDB.getPassword().equals(loginUser.getPassword())) {
            // Successful login
            // Generate a JWT token
            String token = generateToken(userFromDB);

            // Set the token as a cookie with a longer expiration time
            Timestamp expireTime = new Timestamp(System.currentTimeMillis() + (1000L * 3600 * 24 * 7));
            long maxAgeInSeconds = (expireTime.getTime() - System.currentTimeMillis()) / 1000;
            Cookie cookie = new Cookie("authToken", token);
            cookie.setMaxAge((int) maxAgeInSeconds); // 7 days
            cookie.setPath("/"); // Save the cookie for all pages of the site
            cookie.setSecure(true);
            cookie.setDomain("danov-autoshow-656625355b99.herokuapp.com");

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (authenticationTokensRepository.findByUser_Id(userFromDB.getId()) == null) {
                AuthenticationToken authenticationToken = new AuthenticationToken(token, userFromDB, expireTime);
                authenticationTokensRepository.save(authenticationToken);
                response.addCookie(cookie);
            } else if (currentTime
                    .after(authenticationTokensRepository.findByUser_Id(userFromDB.getId()).getExpireDate())) {
                authenticationTokensRepository.updateUserToken(userFromDB.getId(), token, expireTime);
                response.addCookie(cookie);
            } else {
                cookie.setValue(authenticationTokensRepository.findByUser_Id(userFromDB.getId()).getToken());
                response.addCookie(cookie);
            }

            RecentlyViewedToken recentlyViewedToken = new RecentlyViewedToken();
            if (recentlyViewedRepository.findByUser(userFromDB).isEmpty()) {
                recentlyViewedToken.setUser(userFromDB);
                recentlyViewedToken.setExpireDate();
                recentlyViewedRepository.save(recentlyViewedToken);

                Cookie cookieRecentlyViewed = new Cookie("saved_car_params", "");

                // Set the cookie's expiration date based on the token's expireDate
                LocalDateTime expireDateTime = recentlyViewedToken.getExpireDate();
                long secondsUntilExpiration = LocalDateTime.now().until(expireDateTime, SECONDS);

                cookieRecentlyViewed.setMaxAge((int) secondsUntilExpiration);
                cookieRecentlyViewed.setPath("/");
                cookieRecentlyViewed.setSecure(true);
                cookieRecentlyViewed.setDomain("danov-autoshow-656625355b99.herokuapp.com");
                response.addCookie(cookieRecentlyViewed);
            } else {
                RecentlyViewedToken tokenFromDb = recentlyViewedRepository.findByUser(userFromDB).get();
                String modelPaths = tokenFromDb.getRecentlyViewedCars();

                // Use URLEncoder to encode the entire string
                if (modelPaths != null) {
                    String encodedModelPaths = URLEncoder.encode(modelPaths, StandardCharsets.UTF_8);
                    Cookie cookieRecentlyViewed = new Cookie("saved_car_params", encodedModelPaths);

                    // Set the cookie's expiration date based on the token's expireDate
                    LocalDateTime expireDateTime = tokenFromDb.getExpireDate();
                    long secondsUntilExpiration = LocalDateTime.now().until(expireDateTime, SECONDS);

                    cookieRecentlyViewed.setMaxAge((int) secondsUntilExpiration);
                    cookieRecentlyViewed.setPath("/");
                    cookieRecentlyViewed.setSecure(true);
                    cookieRecentlyViewed.setDomain("danov-autoshow-656625355b99.herokuapp.com");
                    response.addCookie(cookieRecentlyViewed);
                }
            }

            System.out.println("Successfully logged in the user.");
            return "redirect:https://danov-autoshow-656625355b99.herokuapp.com"; // Redirect to the home page
        } else {
            // Failed login
            return "redirect:/login?error"; // Redirect back to the login page with an error parameter
        }
    }

    private String generateToken(User user) {
        // Logic to generate a JWT token with user ID in the payload
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId()) //ID in the payload
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 24 * 7 * 1000)) // 7 days
                .signWith(SignatureAlgorithm.HS512, this.SECRET_KEY)
                .compact();
    }
}
