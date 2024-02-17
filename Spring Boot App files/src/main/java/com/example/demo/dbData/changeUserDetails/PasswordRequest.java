package com.example.demo.dbData.changeUserDetails;

public class PasswordRequest {
    private String id;

    private String currentPassword;

    private String newPassword;

    private String authToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getAuthToken() {
        return authToken;
    }
}
