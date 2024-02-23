package com.example.demo.dbData.SecurityConfigs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtils {

    private static final String SECRET_KEY = "312op3v53452jv231VWQE3v167456psdapqovk12opvj2opevjas312jvd1jQWEQWE1V21dsa41241vsada123v41AS23vj1p23v123v1kdpaWEQE12VGGDFKPOGKDPFOs";

    public static String getAuthTokenCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // Return null if authToken cookie is not found
    }

    public static long getUserIdFromAuthToken(String authToken) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken).getBody();
            return claims.get("userId", Long.class);
        } catch (Exception e) {
            // Handle exception (e.g., invalid token)
            return 0L;
        }
    }
}
