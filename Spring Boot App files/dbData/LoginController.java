package com.example.demo.dbData;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

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

    @Autowired
    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginUser", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("loginUser") User loginUser,
                               HttpServletResponse response) {
        // Retrieve the user from the database based on the entered username
        User userFromDB = userRepository.findByUsername(loginUser.getUsername());

        if (userFromDB != null && userFromDB.getPassword().equals(loginUser.getPassword())) {
            // Successful login
            // Generate a JWT token
            String token = generateToken(userFromDB);

            // Set the token as a cookie with a longer expiration time
            Cookie cookie = new Cookie("authToken", token);
            cookie.setMaxAge(3600 * 24 * 7); // 7 days
            cookie.setPath("/"); // Save the cookie for all pages of the site
            cookie.setSecure(true);
            cookie.setDomain("danov-autoshow-656625355b99.herokuapp.com");
            response.addCookie(cookie);

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
