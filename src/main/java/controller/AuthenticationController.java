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
import controller.MainController;
import javafx.application.Application;
import java.util.prefs.Preferences;
import model.RecipeData;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

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
    private void handleSignUp() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean autoLogin = autoLoginCheckBox.isSelected();

        if (isSignUpSuccessful(username)) {
            addUserToDB(username, password);
            navigateToMainView();
        } else {
            setError("That username is taken.");
        }
    }

    public void addUserToDB(String username, String password) {
        String uri = "mongodb+srv://kazbijar:8DpFkd65TNEzaNv7@cluster0.34t1sqc.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {

            MongoDatabase userDB = mongoClient.getDatabase("users");
            userCollection = userDB.getCollection("usernames_passwords");
            insertUser(userCollection, username, password);
        }
    }

    public Boolean isUsernameTaken(String username) {
        String uri = "mongodb+srv://kazbijar:8DpFkd65TNEzaNv7@cluster0.34t1sqc.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase userDB = mongoClient.getDatabase("users");
            userCollection = userDB.getCollection("usernames_passwords");

            // Query the database to see if a user with the given username already exists
            Document found = userCollection.find(new Document("username", username)).first();
            return found != null; // If found is not null, the username is taken
        }
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

    private void navigateToMainView() throws Exception {
        // Load the main view FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();
        Stage primaryStage = (Stage) usernameField.getScene().getWindow();

        primaryStage.setTitle("PantryPal 2");

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.setWidth(800);
        primaryStage.setHeight(500);

        primaryStage.show();

        // Load recipes after showing the primary stage
        MainController controller = loader.getController();
        Platform.runLater(controller::loadRecipes);
    }

    @FXML
    private void handleLogIn() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean autoLogin = autoLoginCheckBox.isSelected();

        if (isUsernameTaken(username)) {
            if (isPasswordCorrect(username, password)) {
                if (autoLoginCheckBox.isSelected()) {
                    saveLoginCredentials(username, password);
                }
                navigateToMainView();
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
        }
        else {
            return false;
        }
    }

    public Boolean isPasswordCorrect(String username, String password) {
        String uri = "mongodb+srv://kazbijar:8DpFkd65TNEzaNv7@cluster0.34t1sqc.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
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
}
