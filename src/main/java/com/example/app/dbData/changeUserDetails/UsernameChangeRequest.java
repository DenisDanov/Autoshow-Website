package com.example.app.dbData.changeUserDetails;

public class UsernameChangeRequest {

    private String userId;
    private String username;
    private String password;

    private String authToken;

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
