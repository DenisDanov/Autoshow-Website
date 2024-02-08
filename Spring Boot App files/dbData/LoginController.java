package com.example.demo.dbData;

import com.example.demo.dbData.recentlyViewedToken.RecentlyViewedRepository;
import com.example.demo.dbData.recentlyViewedToken.RecentlyViewedToken;
import com.example.demo.dbData.unsuccessfulLoginAttempts.ExpiringEntityDeleter;
import com.example.demo.dbData.unsuccessfulLoginAttempts.FailedLoginAttempts;
import com.example.demo.dbData.unsuccessfulLoginAttempts.FailedLoginAttemptsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Controller
public class LoginController {

    private static final String SECRET_KEY = generateRandomKey();

    private static String generateRandomKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[64]; // Token length

        secureRandom.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    private final UserRepository userRepository;

    private final RecentlyViewedRepository recentlyViewedRepository;

    private final AuthenticationTokensRepository authenticationTokensRepository;

    private final FailedLoginAttemptsRepository failedLoginAttemptsRepository;

    private final ExpiringEntityDeleter expiringEntityDeleter;

    @Autowired
    public LoginController(UserRepository userRepository,
                           RecentlyViewedRepository recentlyViewedRepository,
                           AuthenticationTokensRepository authenticationTokensRepository,
                           FailedLoginAttemptsRepository failedLoginAttemptsRepository,
                           ExpiringEntityDeleter expiringEntityDeleter) {
        this.userRepository = userRepository;
        this.recentlyViewedRepository = recentlyViewedRepository;
        this.authenticationTokensRepository = authenticationTokensRepository;
        this.failedLoginAttemptsRepository = failedLoginAttemptsRepository;
        this.expiringEntityDeleter = expiringEntityDeleter;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginUser", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("loginUser") User loginUser,
                               HttpServletResponse response,
                               HttpServletRequest request) {

        // Retrieve the user from the database based on the entered username
        User userFromDB = userRepository.findByUsername(loginUser.getUsername());
        FailedLoginAttempts failedLoginAttempts = null;
        if (userFromDB != null) {
            failedLoginAttempts = failedLoginAttemptsRepository.findByUser_Id(userRepository.findByUsername(userFromDB.getUsername()).getId());
        }

        if (failedLoginAttempts != null) {
            if (getFailedLoginsAmount(getAccountLockCookie(request.getCookies())) >= 20 ||
                    failedLoginAttempts.getAmountFailedLogins() >= 20) {
                if (getFailedLoginsAmount(getAccountLockCookie(request.getCookies())) == 0 ||
                        (getExpireTime(getAccountLockCookie(request.getCookies())) != null &&
                                isWithin30Minutes(getExpireTime(getAccountLockCookie(request.getCookies()))))) {
                    if (getFailedLoginsAmount(getAccountLockCookie(request.getCookies())) <= 20) {
                        Cookie cookie = new Cookie("account_lock", generateTokenFailedLogins(
                                failedLoginAttempts.getAccountLockExpireTime(),
                                failedLoginAttempts.getUser().getId(),
                                failedLoginAttempts.getAmountFailedLogins(),
                                failedLoginAttempts));
                        Date expireDate = new Date(failedLoginAttempts.getAccountLockExpireTime().getTime());
                        cookie.setMaxAge((int) ((expireDate.getTime() - System.currentTimeMillis()) / 1000)); // setMaxAge expects seconds
                        cookie.setPath("/"); // Save the cookie for all pages of the site
                        response.addCookie(cookie);
                    }
                }
                return "redirect:/login?errorToManyFailedAttempts";
            }
        }

        if (userFromDB != null && userFromDB.getPassword().equals(loginUser.getPassword()) &&
                !isUserLocked(failedLoginAttempts) &&
                getFailedLoginsAmount(getAccountLockCookie(request.getCookies())) < 10) {
            // Successful login
            // Generate a JWT token
            String token = generateAuthToken(userFromDB);

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
            } else if (currentTime
                    .after(authenticationTokensRepository.findByUser_Id(userFromDB.getId()).getExpireDate())) {
                authenticationTokensRepository.updateUserToken(userFromDB.getId(), token, expireTime);
            } else {
                cookie.setValue(authenticationTokensRepository.findByUser_Id(userFromDB.getId()).getToken());
            }

            RecentlyViewedToken recentlyViewedToken;
            String encodedModelPaths = "";
            if (recentlyViewedRepository.findByUser_Id(userFromDB.getId()).isEmpty()) {
                recentlyViewedToken = new RecentlyViewedToken();
                recentlyViewedToken.setUser(userFromDB);
                recentlyViewedToken.setExpireDate();
                recentlyViewedRepository.save(recentlyViewedToken);
            } else {
                recentlyViewedToken = recentlyViewedRepository.findByUser_Id(userFromDB.getId()).get();
                String modelPaths = recentlyViewedToken.getRecentlyViewedCars();

                // Use URLEncoder to encode the entire string
                if (modelPaths != null) {
                    encodedModelPaths = URLEncoder.encode(modelPaths, StandardCharsets.UTF_8);
                }
            }

            Cookie cookieRecentlyViewed = new Cookie("saved_car_params", encodedModelPaths);

            // Set the cookie's expiration date based on the token's expireDate
            long expirationInSeconds = recentlyViewedToken.getExpireDate().getTime() / 1000;
            cookieRecentlyViewed.setMaxAge((int) (expirationInSeconds - System.currentTimeMillis() / 1000));
            cookieRecentlyViewed.setPath("/");
            cookieRecentlyViewed.setSecure(true);
            cookieRecentlyViewed.setDomain("danov-autoshow-656625355b99.herokuapp.com");

            System.out.println("Successfully logged in the user.");
            Cookie accountLockCookie = new Cookie("account_lock", "");
            accountLockCookie.setMaxAge(0);
            accountLockCookie.setPath("/");
            response.addCookie(accountLockCookie);
            response.addCookie(cookieRecentlyViewed);
            response.addCookie(cookie);
            return "redirect:https://danov-autoshow-656625355b99.herokuapp.com"; // Redirect to the home page
        } else {
            // Failed login
            if (userFromDB != null) {
                if (failedLoginAttempts == null) {
                    failedLoginAttempts = setNewFailedLoginEntity(userFromDB);
                    failedLoginAttemptsRepository.save(failedLoginAttempts);
                    expiringEntityDeleter.scheduleDeletion(failedLoginAttempts);

                    Cookie existingCookie = getAccountLockCookie(request.getCookies());
                    int amountFailedLogins = 0;

                    Date expireTime = null;
                    if (existingCookie == null) {
                        amountFailedLogins = 1;
                    } else {
                        String cookieValue = existingCookie.getValue();
                        // Decode the JWT token
                        String[] parts = cookieValue.split("\\.");

                        // Decode payload from Base64
                        String payloadJson = base64Decode(parts[1]);

                        // Parse JSON payload into a JsonNode
                        JsonNode payloadNode = parseJson(payloadJson);

                        // Extract values from the JsonNode
                        amountFailedLogins = Objects.requireNonNull(payloadNode).
                                get("failed_logins_amount").asInt() + 1;
                        String expirationDateClaim = payloadNode.get("expirationDate").asText();
                        long timestamp = Long.parseLong(expirationDateClaim);
                        expireTime = new Date(timestamp);
                    }
                    userLoginFailed(loginUser, response, userFromDB, failedLoginAttempts, existingCookie, amountFailedLogins, expireTime);
                } else {
                    if ((failedLoginAttempts.getAmountFailedLogins() + 1) % 10 == 0) {
                        failedLoginAttemptsRepository.save(lockUserAccount(
                                failedLoginAttempts,
                                response,
                                request,
                                failedLoginAttempts.getUser().getId()));
                        expiringEntityDeleter.scheduleDeletion(failedLoginAttempts);

                        return "redirect:/login?errorToManyFailedAttempts";
                    } else {
                        failedLoginAttempts.setAmountFailedLogins(failedLoginAttempts.getAmountFailedLogins() + 1);
                        failedLoginAttemptsRepository.save(failedLoginAttempts);
                        expiringEntityDeleter.scheduleDeletion(failedLoginAttempts);

                        Cookie existingCookie = getAccountLockCookie(request.getCookies());
                        int amountFailedLogins = 0;

                        Date expireTime = null;
                        if (existingCookie == null) {
                            amountFailedLogins = failedLoginAttempts.getAmountFailedLogins();
                        } else {
                            String cookieValue = existingCookie.getValue();
                            // Decode the JWT token
                            String[] parts = cookieValue.split("\\.");

                            // Decode payload from Base64
                            String payloadJson = base64Decode(parts[1]);

                            // Parse JSON payload into a JsonNode
                            JsonNode payloadNode = parseJson(payloadJson);

                            // Extract values from the JsonNode
                            amountFailedLogins = Objects.requireNonNull(payloadNode).
                                    get("failed_logins_amount").
                                    asInt() + 1;
                            String expirationDateClaim = payloadNode.get("expirationDate").asText();
                            long timestamp = Long.parseLong(expirationDateClaim);
                            expireTime = new Date(timestamp);
                        }
                        userLoginFailed(loginUser, response, userFromDB, failedLoginAttempts, existingCookie, amountFailedLogins, expireTime);
                        if (failedLoginAttempts.getAmountFailedLogins() >= 10) {
                            return "redirect:/login?errorToManyFailedAttempts";
                        }
                    }
                }
            } else {
                Cookie existingCookie = getAccountLockCookie(request.getCookies());
                if (existingCookie == null) {
                    lockUnregisteredUser(response, request);
                    return "redirect:/login?error";
                }
                String cookieValue = existingCookie.getValue();
                // Decode the JWT token
                String[] parts = cookieValue.split("\\.");

                // Decode payload from Base64
                String payloadJson = base64Decode(parts[1]);

                // Parse JSON payload into a JsonNode
                JsonNode payloadNode = parseJson(payloadJson);

                // Extract values from the JsonNode
                int amountFailedLogins = Objects.requireNonNull(payloadNode).
                        get("failed_logins_amount").
                        asInt() + 1;
                if (amountFailedLogins <= 21) {
                    if (lockUnregisteredUser(response, request)) {
                        return "redirect:/login?errorToManyFailedAttempts";
                    } else {
                        return "redirect:/login?error";
                    }
                }
            }
            return "redirect:/login?error"; // Redirect back to the login page with an error parameter
        }
    }

    private boolean isUserLocked(FailedLoginAttempts failedLoginAttempts) {
        if (failedLoginAttempts != null) {
            return failedLoginAttempts.isUserLocked();
        } else {
            return false;
        }
    }

    private void userLoginFailed(@ModelAttribute("loginUser") User loginUser, HttpServletResponse response, User userFromDB, FailedLoginAttempts failedLoginAttempts, Cookie existingCookie, int amountFailedLogins, Date expireTime) {
        if (userFromDB.getPassword().equals(loginUser.getPassword())) {
            amountFailedLogins -= 1;
        }
        if (existingCookie != null) {
            existingCookie.setValue(generateTokenFailedLogins(
                    expireTime,
                    failedLoginAttempts.getUser().getId(),
                    amountFailedLogins,
                    failedLoginAttempts));
            existingCookie.setMaxAge((int) ((expireTime.getTime() - System.currentTimeMillis()) / 1000));
            existingCookie.setPath("/");
            response.addCookie(existingCookie);
        } else {
            Cookie cookie = new Cookie("account_lock", generateTokenFailedLogins(
                    failedLoginAttempts.getAccountLockExpireTime(),
                    failedLoginAttempts.getUser().getId(),
                    amountFailedLogins,
                    failedLoginAttempts));
            Date expireDate = new Date(failedLoginAttempts.getAccountLockExpireTime().getTime());
            cookie.setMaxAge((int) ((expireDate.getTime() - System.currentTimeMillis()) / 1000)); // setMaxAge expects seconds
            cookie.setPath("/"); // Save the cookie for all pages of the site
            response.addCookie(cookie);
        }
    }

    private static FailedLoginAttempts setNewFailedLoginEntity(User userFromDB) {
        Calendar calendar = Calendar.getInstance();

        // Add 30 minutes
        calendar.add(Calendar.MINUTE, 30);

        // new timestamp
        Timestamp futureTimestamp = new Timestamp(calendar.getTime().getTime());
        FailedLoginAttempts failedLoginAttempts = new FailedLoginAttempts(1,
                false,
                futureTimestamp,
                userFromDB);
        return failedLoginAttempts;
    }

    private static FailedLoginAttempts lockUserAccount(FailedLoginAttempts failedLoginAttempts,
                                                       HttpServletResponse response,
                                                       HttpServletRequest request,
                                                       Long userId) {
        int amount = failedLoginAttempts.getAmountFailedLogins() + 1;
        Calendar calendar = Calendar.getInstance();
        if (failedLoginAttempts.getAmountFailedLogins() == 9) {
            // Add 30 minutes
            calendar.add(Calendar.MINUTE, 30);
            failedLoginAttempts.setAmountFailedLogins(amount);
        } else {
            // Add 60 minutes
            calendar.add(Calendar.MINUTE, 60);
            failedLoginAttempts.setAmountFailedLogins(amount);
        }

        // Get the new timestamp
        Timestamp futureTimestamp = new Timestamp(calendar.getTime().getTime());
        failedLoginAttempts.setUserLocked(true);
        failedLoginAttempts.setAccountLockExpireTime(futureTimestamp);

        // Convert Timestamp to Date
        Date expireDate = new Date(futureTimestamp.getTime());

        Cookie existingCookie = getAccountLockCookie(request.getCookies());
        int amountFailedLogins = 0;

        if (existingCookie == null) {
            amountFailedLogins = failedLoginAttempts.getAmountFailedLogins();
        } else {
            String cookieValue = existingCookie.getValue();
            // Decode the JWT token
            String[] parts = cookieValue.split("\\.");

            // Decode payload from Base64
            String payloadJson = base64Decode(parts[1]);

            // Parse JSON payload into a JsonNode
            JsonNode payloadNode = parseJson(payloadJson);

            // Extract values from the JsonNode
            amountFailedLogins = Objects.requireNonNull(payloadNode).get("failed_logins_amount").asInt() + 1;
            String expirationDateClaim = payloadNode.get("expirationDate").asText();
            long timestamp = Long.parseLong(expirationDateClaim);
            if (!isWithin30Minutes(new Date(timestamp))) {
                expireDate = new Date(timestamp);
            }
        }

        // Set the cookie with the formatted string
        Cookie cookie = new Cookie("account_lock", generateTokenFailedLogins(expireDate, userId, amountFailedLogins, failedLoginAttempts));
        cookie.setMaxAge((int) ((expireDate.getTime() - System.currentTimeMillis()) / 1000)); // setMaxAge expects seconds
        cookie.setPath("/"); // Save the cookie for all pages of the site

        // Add the cookie to the response
        response.addCookie(cookie);
        return failedLoginAttempts;
    }

    private boolean lockUnregisteredUser(HttpServletResponse response, HttpServletRequest request) {
        Cookie cookie = getAccountLockCookie(request.getCookies());
        if (cookie != null) {
            String cookieValue = cookie.getValue();
            // Decode the JWT token
            String[] parts = cookieValue.split("\\.");

            // Decode payload from Base64
            String payloadJson = base64Decode(parts[1]);

            // Parse JSON payload into a JsonNode
            JsonNode payloadNode = parseJson(payloadJson);

            // Extract values from the JsonNode
            int failedLoginsAmount = payloadNode.get("failed_logins_amount").asInt() + 1;
            String expirationDateClaim = payloadNode.get("expirationDate").asText();
            long timestamp = Long.parseLong(expirationDateClaim);

            Date expireDate = new Date(timestamp);

            if (failedLoginsAmount >= 20) {
                Date currentTime = new Date();
                // Create a Calendar instance and set it to the current time
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentTime);

                // Add 60 minutes to the current time
                calendar.add(Calendar.MINUTE, 60);

                expireDate = calendar.getTime();
            }
            Cookie newCookie = new Cookie("account_lock", generateTokenFailedLogins(expireDate, null, failedLoginsAmount, null));
            newCookie.setMaxAge((int) ((expireDate.getTime() - System.currentTimeMillis()) / 1000)); // setMaxAge expects seconds
            newCookie.setPath("/"); // Save the cookie for all pages of the site
            response.addCookie(newCookie);
            return failedLoginsAmount >= 10;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 30);
            Timestamp futureTimestamp = new Timestamp(calendar.getTime().getTime());
            Date expireDate = new Date(futureTimestamp.getTime());
            Cookie newCookie = new Cookie("account_lock", generateTokenFailedLogins(expireDate, null, 1, null));
            newCookie.setMaxAge((int) ((expireDate.getTime() - System.currentTimeMillis()) / 1000)); // setMaxAge expects seconds
            newCookie.setPath("/"); // Save the cookie for all pages of the site
            response.addCookie(newCookie);
            return false;
        }
    }

    private int getFailedLoginsAmount(Cookie cookie) {
        if (cookie == null) {
            return 0;
        }
        String cookieValue = cookie.getValue();
        // Decode the JWT token
        String[] parts = cookieValue.split("\\.");

        // Decode payload from Base64
        String payloadJson = base64Decode(parts[1]);

        // Parse JSON payload into a JsonNode
        JsonNode payloadNode = parseJson(payloadJson);

        // Extract values from the JsonNode
        return Objects.requireNonNull(payloadNode).
                get("failed_logins_amount").
                asInt();
    }

    private static Date getExpireTime(Cookie cookie) {
        if (cookie == null) {
            return null;
        }
        String cookieValue = cookie.getValue();
        // Decode the JWT token
        String[] parts = cookieValue.split("\\.");

        // Decode payload from Base64
        String payloadJson = base64Decode(parts[1]);

        // Parse JSON payload into a JsonNode
        JsonNode payloadNode = parseJson(payloadJson);

        String expirationDateClaim = payloadNode.get("expirationDate").asText();
        long timestamp = Long.parseLong(expirationDateClaim);

        return new Date(timestamp);
    }

    private static boolean isWithin30Minutes(Date expirationDate) {
        // Get the current time
        Date currentTime = new Date();

        // Calculate the time difference in milliseconds
        long timeDifference = expirationDate.getTime() - currentTime.getTime();

        // Check if the time difference is less than 30 minutes (30 * 60 * 1000 milliseconds)
        return timeDifference < (30 * 60 * 1000);
    }

    private static Cookie getAccountLockCookie(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("account_lock".equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    private String generateAuthToken(User user) {
        // Logic to generate a JWT token with user ID in the payload
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId()) //ID in the payload
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 24 * 7 * 1000)) // 7 days
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    private static String generateTokenFailedLogins(Date expirationDate, Long userId, int failedTimes,
                                                    FailedLoginAttempts failedLoginAttempts) {
        boolean isUserLocked = false;
        if (failedLoginAttempts != null) {
            isUserLocked = failedLoginAttempts.isUserLocked();
        } else if (failedTimes >= 10) {
            isUserLocked = true;
        }
        return Jwts.builder()
                .setExpiration(expirationDate)
                .claim("expirationDate", expirationDate.getTime())
                .claim("id", userId)
                .claim("failed_logins_amount", failedTimes)
                .claim("isUserLocked", isUserLocked)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

    }

    private static String base64Decode(String input) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(input);
        return new String(decodedBytes);
    }

    private static JsonNode parseJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

