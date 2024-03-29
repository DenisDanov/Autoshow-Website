package com.example.app.controllers.utils;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthTokenValidationUtil {

    boolean isAuthTokenValid(long userId, String authToken, HttpServletResponse response);

    boolean isAuthTokenValid(long userId, String authToken);

    String getValidAuthToken(long userId);
}
