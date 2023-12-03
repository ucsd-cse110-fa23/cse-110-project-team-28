package model;

import org.json.JSONObject;

/**
 * Singleton class that stores the current user's data
 */
public class UserData {
    private static UserData instance;

    private String userId;
    private String username;

    public static UserData getInstance() {
        return instance;
    }

    public static UserData setInstance(UserData userData) {
        instance = userData;
        return instance;
    }

    public UserData setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserData setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public String getUserId() {
        return this.userId;
    }

    public static UserData fromJSON(JSONObject responseJSON) {
        UserData userData = new UserData();

        userData.setUserId(responseJSON.getString("_id"));

        return userData;
    }

    @Override
    public String toString() {
        return "UserData [userId=" + userId + ", username=" + username + "]";
    }
}