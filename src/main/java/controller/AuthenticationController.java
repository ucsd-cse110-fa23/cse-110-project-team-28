package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.IOException;
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

        try {
            if (username.isEmpty() || password.isEmpty()) {
                setError("Please enter a username and password.");
                return;
            }

            if (isSignUpSuccessful(username)) {
                addUserToDB(username, password);
                SceneHelper.switchToMainScene(usernameField.getScene()); // Navigate to main only if sign up is successful
            } else {
                setError("That username is taken.");
            }
        } catch (IOException e) {
            showErrorPopup("Error connecting to the server. Please try again later.");
        } catch (Exception e) {
            showErrorPopup("An unexpected error occurred.");
        }
    }

    public void addUserToDB(String username, String password) throws IOException{
        MongoClient mongoClient = MongoDB.getMongoClient();

        MongoDatabase userDB = mongoClient.getDatabase("users");
        userCollection = userDB.getCollection("usernames_passwords");
        insertUser(userCollection, username, password);

    }

    public Boolean isUsernameTaken(String username) throws IOException{
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

    private boolean isSignUpSuccessful(String username) throws IOException{
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

        try {
            if (username.isEmpty() || password.isEmpty()) {
                setError("Please enter a username and password.");
                return;
            }

            if (isLogInSuccessful(username, password)) {
                if (autoLoginCheckBox.isSelected()) {
                    saveLoginCredentials(username, password);
                }
                SceneHelper.switchToMainScene(usernameField.getScene()); // Navigate to main only if login is successful
                RecipeData.getInstance().loadRecipes(username); // load recipes from db
            } else {
                setError("Incorrect username or password.");
            }
        } catch (IOException e) {
            showErrorPopup("Error connecting to the server. Please try again later.");
        } catch (Exception e) {
            showErrorPopup("An unexpected error occurred.");
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

    private boolean isLogInSuccessful(String username, String password) throws IOException{
        if (isPasswordCorrect(username, password)) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isPasswordCorrect(String username, String password) throws IOException{
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

    public void showErrorPopup(String error) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/errorPopup.fxml"));
            Parent root = loader.load();

            ErrorController controller = loader.getController();
            controller.setErrorMessage(error);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            // handle exception
        }
    }
}