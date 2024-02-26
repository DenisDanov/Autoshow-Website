package com.example.demo.dbData;

import com.example.demo.dbData.ReplacedTokens.ReplacedAuthTokens;
import com.example.demo.dbData.ReplacedTokens.ReplacedAuthTokensRepo;
import com.example.demo.dbData.recentlyViewedToken.RecentlyViewedRepository;
import com.example.demo.dbData.recentlyViewedToken.RecentlyViewedToken;
import com.example.demo.dbData.unsuccessfulLoginAttempts.ExpiringEntityDeleter;
import com.example.demo.dbData.unsuccessfulLoginAttempts.FailedLoginAttempts;
import com.example.demo.dbData.unsuccessfulLoginAttempts.FailedLoginAttemptsRepository;
import com.example.demo.emailApp.SendGridEmailService;
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

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class LoginController {

    private static final String SECRET_KEY = "312op3v53452jv231VWQE3v167456psdapqovk12opvj2opevjas312jvd1jQWEQWE1V21dsa41241vsada123v41AS23vj1p23v123v1kdpaWEQE12VGGDFKPOGKDPFOs";

    private final UserRepository userRepository;

    private final RecentlyViewedRepository recentlyViewedRepository;

    private final AuthenticationTokenRepository authenticationTokenRepository;

    private final FailedLoginAttemptsRepository failedLoginAttemptsRepository;

    private final ExpiringEntityDeleter expiringEntityDeleter;

    private final ReplacedAuthTokensRepo replacedAuthTokensRepo;

    private final SendGridEmailService sendGridEmailService;

    @Autowired
    public LoginController(UserRepository userRepository,
                           RecentlyViewedRepository recentlyViewedRepository,
                           AuthenticationTokenRepository authenticationTokenRepository,
                           FailedLoginAttemptsRepository failedLoginAttemptsRepository,
                           ExpiringEntityDeleter expiringEntityDeleter,
                           ReplacedAuthTokensRepo replacedAuthTokensRepo,
                           SendGridEmailService sendGridEmailService) {
        this.userRepository = userRepository;
        this.recentlyViewedRepository = recentlyViewedRepository;
        this.authenticationTokenRepository = authenticationTokenRepository;
        this.failedLoginAttemptsRepository = failedLoginAttemptsRepository;
        this.expiringEntityDeleter = expiringEntityDeleter;
        this.replacedAuthTokensRepo = replacedAuthTokensRepo;
        this.sendGridEmailService = sendGridEmailService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginUser", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("loginUser") User loginUser,
                               HttpServletResponse response,
                               HttpServletRequest request) throws IOException {

        // Retrieve the user from the database based on the entered username
        User userFromDB = userRepository.findByUsername(loginUser.getUsername());
        FailedLoginAttempts failedLoginAttempts = null;
        // maybe stop the db queries if the user has 20 unsuccessful attempts
        if (userFromDB != null) {
            failedLoginAttempts = failedLoginAttemptsRepository.findByUser_Id(userRepository.findByUsername(userFromDB.getUsername()).getId());
        }

        if (failedLoginAttempts != null && failedLoginAttempts.getAmountFailedLogins() >= 20 &&
                (getExpireTime(getAccountLockCookie(request.getCookies())) == null)) {
            Cookie cookie = new Cookie("account_lock", generateTokenFailedLogins(
                    failedLoginAttempts.getAccountLockExpireTime(),
                    failedLoginAttempts.getUser().getId(),
                    failedLoginAttempts.getAmountFailedLogins(),
                    failedLoginAttempts));
            Date expireDate = new Date(failedLoginAttempts.getAccountLockExpireTime().getTime());
            cookie.setMaxAge((int) ((expireDate.getTime() - System.currentTimeMillis()) / 1000)); // setMaxAge expects seconds
            cookie.setPath("/"); // Save the cookie for all pages of the site
            response.addCookie(cookie);
            return "redirect:/login?errorTooManyFailedAttempts";
        } else if ((getFailedLoginsAmount(getAccountLockCookie(request.getCookies())) >= 20) ||
                (failedLoginAttempts != null && failedLoginAttempts.getAmountFailedLogins() >= 20)) {
            return "redirect:/login?errorTooManyFailedAttempts";
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
            AuthenticationToken authenticationToken = authenticationTokenRepository.findByUser_Id(userFromDB.getId());
            if (authenticationToken == null) {
                authenticationToken = new AuthenticationToken(token, userFromDB, expireTime);
                replacedAuthTokensRepo.save(new ReplacedAuthTokens(userFromDB,authenticationToken.getToken(),authenticationToken.getExpireDate()));
                authenticationTokenRepository.save(authenticationToken);
            } else if (currentTime
                    .after(authenticationToken.getExpireDate())) {
                authenticationTokenRepository.updateUserToken(userFromDB.getId(), token, expireTime);
            } else {
                replacedAuthTokensRepo.save(new ReplacedAuthTokens(userFromDB,
                        authenticationToken.getToken(),
                        authenticationToken.getExpireDate()));
                authenticationTokenRepository.updateUserToken(userFromDB.getId(), token, expireTime);
            }

            RecentlyViewedToken recentlyViewedToken;
            String encodedModelPaths = "";
            if (recentlyViewedRepository.findByUser_Id(userFromDB.getId()).isEmpty()) {
                recentlyViewedToken = new RecentlyViewedToken();
                recentlyViewedToken.setRecentlyViewedCars("");
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

            Cookie cookieRecentlyViewed = new Cookie("recently_viewed", encodedModelPaths);

            // Set the cookie's expiration date based on the token's expireDate
            expireTime = new Timestamp(recentlyViewedToken.getExpireDate().getTime());
            maxAgeInSeconds = (expireTime.getTime() - System.currentTimeMillis()) / 1000;
            cookieRecentlyViewed.setMaxAge((int) maxAgeInSeconds);
            cookieRecentlyViewed.setPath("/"); // Save the cookie for all pages of the site

            System.out.println("Successfully logged in the user.");

            Cookie accountLockCookie = new Cookie("account_lock", "");
            accountLockCookie.setMaxAge(0);
            accountLockCookie.setPath("/");
            response.addCookie(accountLockCookie);
            response.addCookie(cookie);
            response.addCookie(cookieRecentlyViewed);
            return "redirect:https://danov-autoshow-656625355b99.herokuapp.com"; // Redirect to the home page
        } else {
            // Failed login
            if (userFromDB != null) {
                if (failedLoginAttempts == null) {
                    if (!userFromDB.getPassword().equals(loginUser.getPassword())) {
                        failedLoginAttempts = setNewFailedLoginEntity(userFromDB);
                        failedLoginAttemptsRepository.save(failedLoginAttempts);
                        expiringEntityDeleter.cancelScheduledDeletion(failedLoginAttempts);
                        expiringEntityDeleter.scheduleDeletion(failedLoginAttempts);
                    }

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
                        if (timestamp == 0 && amountFailedLogins >= 10) {
                            TimeZone sofiaTimeZone = TimeZone.getTimeZone("Europe/Sofia");
                            Calendar calendar = Calendar.getInstance(sofiaTimeZone);

                            // Add 30 minutes
                            if (amountFailedLogins < 20) {
                                // Add 30 minutes
                                calendar.add(Calendar.MINUTE, 30); // for 30 minutes from now
                            } else {
                                // Add 30 minutes
                                calendar.add(Calendar.MINUTE, 60); // for 30 minutes from now
                            }
                            timestamp = new Timestamp(calendar.getTime().getTime()).getTime();
                        }
                        expireTime = new Date(timestamp);
                    }
                    if (!userFromDB.getPassword().equals(loginUser.getPassword())) {
                        userLoginFailed(loginUser,
                                response,
                                userFromDB,
                                failedLoginAttempts,
                                existingCookie,
                                amountFailedLogins,
                                expireTime);
                    }
                } else {

                    if ((failedLoginAttempts.getAmountFailedLogins() + 1) % 10 == 0 &&
                            !userFromDB.getPassword().equals(loginUser.getPassword())) {
                        failedLoginAttemptsRepository.save(lockUserAccount(
                                failedLoginAttempts,
                                response,
                                request,
                                failedLoginAttempts.getUser().getId()));
                        expiringEntityDeleter.cancelScheduledDeletion(failedLoginAttempts);
                        expiringEntityDeleter.scheduleDeletion(failedLoginAttempts);
                        System.out.println(new Date(failedLoginAttempts.getAccountLockExpireTime().getTime()));
                        System.out.println(failedLoginAttempts.getAccountLockExpireTime().getTime() - System.currentTimeMillis());
                        return "redirect:/login?errorTooManyFailedAttempts";
                    } else {
                        if (!userFromDB.getPassword().equals(loginUser.getPassword())) {
                            failedLoginAttempts.setAmountFailedLogins(failedLoginAttempts.getAmountFailedLogins() + 1);
                            failedLoginAttemptsRepository.save(failedLoginAttempts);
                        }

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
                            if (timestamp == 0 && amountFailedLogins >= 10) {
                                TimeZone sofiaTimeZone = TimeZone.getTimeZone("Europe/Sofia");
                                Calendar calendar = Calendar.getInstance(sofiaTimeZone);

                                if (amountFailedLogins < 20) {
                                    // Add 30 minutes
                                    calendar.add(Calendar.MINUTE, 30); // for 30 minutes from now
                                } else {
                                    // Add 30 minutes
                                    calendar.add(Calendar.MINUTE, 60); // for 30 minutes from now
                                }

                                timestamp = new Timestamp(calendar.getTime().getTime()).getTime();
                            } else if (failedLoginAttempts.getAmountFailedLogins() >= 10 &&
                                    amountFailedLogins < 10) {
                                TimeZone sofiaTimeZone = TimeZone.getTimeZone("Europe/Sofia");
                                Calendar calendar = Calendar.getInstance(sofiaTimeZone);

                                // Add 30 minutes
                                calendar.add(Calendar.MINUTE, 30); // for 30 minutes from now

                                timestamp = new Timestamp(calendar.getTime().getTime()).getTime();
                                amountFailedLogins = failedLoginAttempts.getAmountFailedLogins();
                            }
                            expireTime = new Date(timestamp);
                        }
                        if (!userFromDB.getPassword().equals(loginUser.getPassword()) ||
                                failedLoginAttempts.isUserLocked()) {
                            userLoginFailed(loginUser,
                                    response,
                                    userFromDB,
                                    failedLoginAttempts,
                                    existingCookie,
                                    amountFailedLogins,
                                    expireTime);
                        }
                        if (failedLoginAttempts.getAmountFailedLogins() >= 10) {
                            return "redirect:/login?errorTooManyFailedAttempts";
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
                        return "redirect:/login?errorTooManyFailedAttempts";
                    } else {
                        return "redirect:/login?error";
                    }
                }
            }

            if ((failedLoginAttempts != null && failedLoginAttempts.getAmountFailedLogins() >= 10) ||
                    (getFailedLoginsAmount(getAccountLockCookie(request.getCookies())) >= 10)) {
                return "redirect:/login?errorTooManyFailedAttempts";
            } else {
                return "redirect:/login?error";
            }
        }
    }

    private boolean isUserLocked(FailedLoginAttempts failedLoginAttempts) {
        if (failedLoginAttempts != null) {
            return failedLoginAttempts.isUserLocked();
        } else {
            return false;
        }
    }

    private void userLoginFailed(@ModelAttribute("loginUser") User loginUser,
                                 HttpServletResponse response,
                                 User userFromDB,
                                 FailedLoginAttempts failedLoginAttempts,
                                 Cookie existingCookie,
                                 int amountFailedLogins,
                                 Date expireTime) {
        if (userFromDB.getPassword().equals(loginUser.getPassword()) &&
                failedLoginAttempts.getAmountFailedLogins() < 10) {
            amountFailedLogins -= 1;
        }
        if (existingCookie != null) {
            existingCookie.setValue(generateTokenFailedLogins(
                    expireTime,
                    failedLoginAttempts.getUser().getId(),
                    amountFailedLogins,
                    failedLoginAttempts));
            if (failedLoginAttempts.getAmountFailedLogins() < 10) {
                existingCookie.setMaxAge(1800);
            } else {
                existingCookie.setMaxAge((int) ((expireTime.getTime() - System.currentTimeMillis()) / 1000));
            }
            existingCookie.setPath("/");
            response.addCookie(existingCookie);
        } else {
            Cookie cookie;
            Date expireDate = new Date(failedLoginAttempts.getAccountLockExpireTime().getTime());
            if (!failedLoginAttempts.isUserLocked()) {
                cookie = new Cookie("account_lock", generateTokenFailedLogins(
                        null,
                        failedLoginAttempts.getUser().getId(),
                        amountFailedLogins,
                        failedLoginAttempts));
            } else {
                cookie = new Cookie("account_lock", generateTokenFailedLogins(
                        failedLoginAttempts.getAccountLockExpireTime(),
                        failedLoginAttempts.getUser().getId(),
                        amountFailedLogins,
                        failedLoginAttempts));
            }

            cookie.setMaxAge((int) ((expireDate.getTime() - System.currentTimeMillis()) / 1000)); // setMaxAge expects seconds
            cookie.setPath("/"); // Save the cookie for all pages of the site
            response.addCookie(cookie);
        }
    }

    private static FailedLoginAttempts setNewFailedLoginEntity(User userFromDB) {
        TimeZone sofiaTimeZone = TimeZone.getTimeZone("Europe/Sofia");
        Calendar calendar = Calendar.getInstance(sofiaTimeZone);

        // Add 30 minutes
        calendar.add(Calendar.MINUTE, 30); // for 30 minutes from now

        // new timestamp
        Timestamp futureTimestamp = new Timestamp(calendar.getTime().getTime());
        FailedLoginAttempts failedLoginAttempts = new FailedLoginAttempts(1,
                false,
                futureTimestamp,
                userFromDB);
        return failedLoginAttempts;
    }

    private FailedLoginAttempts lockUserAccount(FailedLoginAttempts failedLoginAttempts,
                                                       HttpServletResponse response,
                                                       HttpServletRequest request,
                                                       Long userId) throws IOException {
        int amount = failedLoginAttempts.getAmountFailedLogins() + 1;
        TimeZone sofiaTimeZone = TimeZone.getTimeZone("Europe/Sofia");
        Calendar calendar = Calendar.getInstance(sofiaTimeZone);

        if (failedLoginAttempts.getAmountFailedLogins() == 9) {
            // Add 30 minutes
            calendar.add(Calendar.MINUTE, 30); // for 30 minutes from now
        } else {
            // Add 60 minutes
            calendar.add(Calendar.MINUTE, 60);
            sendGridEmailService.sendAccountLockEmail(failedLoginAttempts.getUser().getEmail(),new Timestamp(calendar.getTime().getTime()));
        }
        failedLoginAttempts.setAmountFailedLogins(amount);

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
            String lock = payloadNode.get("isUserLocked").asText();
            long timestamp = Long.parseLong(expirationDateClaim);
            if (amount < 20 && amount != 10) {
                expireDate = new Date(timestamp);
            } else if (amountFailedLogins < 10) {
                amountFailedLogins = amount;
            } else if (amount == 10 && lock.equals("true")) {
                expireDate = new Date(timestamp);
            } else if (amount == 20 && amountFailedLogins < 20) {
                amountFailedLogins = amount;
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
                TimeZone sofiaTimeZone = TimeZone.getTimeZone("Europe/Sofia");
                Calendar calendar = Calendar.getInstance(sofiaTimeZone);

                calendar.add(Calendar.MINUTE, 60); // for 60 minutes from now

                expireDate = calendar.getTime();
            } else if (timestamp == 0 && failedLoginsAmount == 10) {
                TimeZone sofiaTimeZone = TimeZone.getTimeZone("Europe/Sofia");
                Calendar calendar = Calendar.getInstance(sofiaTimeZone);

                calendar.add(Calendar.MINUTE, 30); // for 30 minutes from now

                // Convert to Date object
                expireDate = calendar.getTime();
            }

            Cookie newCookie = new Cookie("account_lock", generateTokenFailedLogins(expireDate, null, failedLoginsAmount, null));

            if (failedLoginsAmount < 10) {
                newCookie.setMaxAge(1800); // setMaxAge expects seconds
            } else {
                newCookie.setMaxAge((int) ((expireDate.getTime() - System.currentTimeMillis()) / 1000)); // setMaxAge expects seconds
            }
            newCookie.setPath("/"); // Save the cookie for all pages of the site
            response.addCookie(newCookie);
            return failedLoginsAmount >= 10;
        } else {
            TimeZone sofiaTimeZone = TimeZone.getTimeZone("Europe/Sofia");
            Calendar calendar = Calendar.getInstance(sofiaTimeZone);

            calendar.add(Calendar.MINUTE, 30); // for 30 minutes from now

            // Convert to Date object
            Date expireDate = calendar.getTime();

            // Convert to seconds for setting max age in cookie
            long maxAgeSeconds = (expireDate.getTime() - System.currentTimeMillis()) / 1000;

            // Create cookie
            Cookie newCookie = new Cookie("account_lock", generateTokenFailedLogins(null, null, 1, null));
            newCookie.setMaxAge((int) maxAgeSeconds);
            newCookie.setPath("/");
            System.out.println(newCookie.getMaxAge());
            // Add the cookie to the response
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

    private String generateAuthToken(User user) {
        // Logic to generate a JWT token with user ID in the payload
        return Jwts.builder()
                .claim("userId", user.getId()) //ID in the payload
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 24 * 7 * 1000)) // 7 days
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

}

