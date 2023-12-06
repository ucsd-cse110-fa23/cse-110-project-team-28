package utilites;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.JSONObject;

import model.UserData;

public class AutoLoginHelper {
    public static UserData getUserData() {
        // check for login.json file
        File loginFile = new File("login.json");

        if (!loginFile.exists()) {
            Logger.log("No login file found");
            return null;
        }

        // read login.json file
        FileReader reader;

        try {
            reader = new FileReader(loginFile);

            int data;
            StringBuilder loginData = new StringBuilder();

            while ((data = reader.read()) != -1) {
                loginData.append((char) data);
            }

            reader.close();

            // parse login.json file
            JSONObject loginJson = new JSONObject(loginData.toString());

            String username = loginJson.getString("username");
            String userId = loginJson.getString("userId");

            // login
            Logger.log("Auto-logging in (username: " + username + ", id: " + userId + ")");

            UserData userData = new UserData()
                    .setUserId(userId)
                    .setUsername(username);

            return userData;
        } catch (Exception e) {
            Logger.log("Failed to read login file");
            return null;
        }
    }

    public static void saveUserData(UserData userData) {
        // create login.json file
        JSONObject loginJson = new JSONObject()
                .put("username", userData.getUsername())
                .put("userId", userData.getUserId());

        File loginFile = new File("login.json");

        try {
            loginFile.createNewFile();

            FileWriter writer = new FileWriter(loginFile);
            writer.write(loginJson.toString());
            writer.close();
        } catch (Exception e) {
            Logger.log("Failed to create login file");
        }
    }

    public static void deleteUserData() {
        File loginFile = new File("login.json");

        if (loginFile.exists()) {
            loginFile.delete();
        }
    }
}
