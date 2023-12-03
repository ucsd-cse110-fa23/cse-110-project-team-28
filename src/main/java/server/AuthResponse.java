package server;

import model.UserData;

public class AuthResponse {
    private boolean success;
    private String message;
    private UserData userData;

    public AuthResponse setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public AuthResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public AuthResponse setUserData(UserData userData) {
        this.userData = userData;
        return this;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }

    public UserData getUserData() {
        return this.userData;
    }
}