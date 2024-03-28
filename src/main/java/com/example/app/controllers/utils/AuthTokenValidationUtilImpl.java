package com.example.app.controllers.utils;

import com.example.app.data.entities.AuthenticationToken;
import com.example.app.data.entities.User;
import com.example.app.services.AuthenticationTokenService;
import com.example.app.services.ReplacedAuthTokensService;
import com.example.app.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthTokenValidationUtilImpl implements AuthTokenValidationUtil {

    private final AuthenticationTokenService authenticationTokenService;

    private final ReplacedAuthTokensService replacedAuthTokensService;

    private final UserService userService;

    public AuthTokenValidationUtilImpl(AuthenticationTokenService authenticationTokenService,
                                       ReplacedAuthTokensService replacedAuthTokensService,
                                       UserService userService) {
        this.authenticationTokenService = authenticationTokenService;
        this.replacedAuthTokensService = replacedAuthTokensService;
        this.userService = userService;
    }

    @Override
    public boolean isAuthTokenValid(long userId, String authToken, HttpServletResponse response) {
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isPresent()) {
            AuthenticationToken authenticationToken = authenticationTokenService.findByUser_Id(userId);
            if (authenticationToken != null && authenticationToken.getToken().equals(authToken)) {
                return true;
            } else if ((authenticationToken != null && replacedAuthTokensService.findByReplacedToken(authToken) != null) &&
                    replacedAuthTokensService.findByReplacedToken(authToken).getUser().getId().equals(userId)) {

                replacedAuthTokensService.deleteByReplacedToken(authToken);
                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site

                response.addCookie(cookie);
                return true;
            }
        }
        Cookie cookie = new Cookie("authToken", "");
        Cookie cookieRecentlyViewed = new Cookie("recently_viewed", "");
        cookieRecentlyViewed.setMaxAge(0);
        cookieRecentlyViewed.setPath("/");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.addCookie(cookieRecentlyViewed);
        return false;
    }

    @Override
    public boolean isAuthTokenValid(long userId, String authToken) {
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isPresent() && authenticationTokenService.findByUser_Id(userId) != null) {
            return authenticationTokenService.findByUser_Id(userId).getToken().equals(authToken) ||
                    (replacedAuthTokensService.findByReplacedToken(authToken) != null &&
                            replacedAuthTokensService.findByReplacedToken(authToken).getUser().getId().equals(userId));
        }
        return false;
    }
}
