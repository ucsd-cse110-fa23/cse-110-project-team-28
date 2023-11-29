package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.util.prefs.Preferences;
import model.RecipeData;
import utilites.MongoDB;
import utilites.SceneHelper;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import config.Config;

import org.bson.Document;

public class AuthenticationController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox autoLoginCheckBox;

    @FXML
    private Label errorLabel;

    private MongoCollection<Document> userCollection;

    @FXML
    private void signUpHandler() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            setError("Please enter a username and password.");
            return;
        }

        boolean autoLogin = autoLoginCheckBox.isSelected();

        if (isSignUpSuccessful(username)) {
            addUserToDB(username, password);
            SceneHelper.switchToMainScene(usernameField.getScene());
        } else {
            setError("That username is taken.");
        }
    }

    public void addUserToDB(String username, String password) {
        MongoClient mongoClient = MongoDB.getMongoClient();

        MongoDatabase userDB = mongoClient.getDatabase("users");
        userCollection = userDB.getCollection("usernames_passwords");
        insertUser(userCollection, username, password);

    }

    public Boolean isUsernameTaken(String username) {
        MongoClient mongoClient = MongoDB.getMongoClient();

        MongoDatabase userDB = mongoClient.getDatabase("users");
        userCollection = userDB.getCollection("usernames_passwords");

        // Query the database to see if a user with the given username already exists
        Document found = userCollection.find(new Document("username", username)).first();
        return found != null; // If found is not null, the username is taken
    }

    public void insertUser(MongoCollection<Document> userCollection, String username, String password) {
        Document newUser = new Document("username", username)
                .append("password", password);
        userCollection.insertOne(newUser);
        System.out.println("User " + username + " inserted.");
    }

    private boolean isSignUpSuccessful(String username) {
        // Return true if sign-up is successful
        if (!isUsernameTaken(username)) {
            return true;
        } else {
            return false;
        }
    }

    @FXML
    private void loginHandler() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            setError("Please enter a username and password.");
            return;
        }

        boolean autoLogin = autoLoginCheckBox.isSelected();

        if (isUsernameTaken(username)) {
            if (isPasswordCorrect(username, password)) {
                if (autoLoginCheckBox.isSelected()) {
                    saveLoginCredentials(username, password);
                }
                SceneHelper.switchToMainScene(usernameField.getScene());
            } else {
                setError("Incorrect password.");
            }
        } else {
            setError("Username does not exist.");
        }
    }

    private void saveLoginCredentials(String username, String password) {
        Preferences prefs = Preferences.userNodeForPackage(AuthenticationController.class);
        prefs.put("username", username);
        prefs.put("password", password);
    }

    private void setError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private boolean isLogInSuccessful(String username, String password) {
        if (isPasswordCorrect(username, password)) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isPasswordCorrect(String username, String password) {
        MongoClient mongoClient = MongoDB.getMongoClient();

        MongoDatabase userDB = mongoClient.getDatabase("users");
        userCollection = userDB.getCollection("usernames_passwords");

        // Query the database for the user with the given username
        Document user = userCollection.find(Filters.eq("username", username)).first();

        if (user != null) {
            String storedPassword = user.getString("password");
            return storedPassword.equals(password);
        }

        return false; // User not found or password doesn't match

    }
}
