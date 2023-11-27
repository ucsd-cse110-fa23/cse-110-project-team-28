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
import controller.MainController;
import javafx.application.Application;
import model.RecipeData;

public class AuthenticationController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox autoLoginCheckBox;

    @FXML
    private void handleSignUp() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean autoLogin = autoLoginCheckBox.isSelected();

        // Add logic to handle sign up (validate input, create user account)

        // If the sign-up is successful, navigate to the main view
        if (isSignUpSuccessful(username, password)) {
            navigateToMainView();
        }
    }

    private boolean isSignUpSuccessful(String username, String password) {
        // Placeholder for sign-up logic
        // Return true if sign-up is successful
        return true;
    }

    private void navigateToMainView() throws Exception {
        // Load the main view FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();
        Stage primaryStage = (Stage) usernameField.getScene().getWindow();

        primaryStage.setTitle("PantryPal");

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

    private void saveLoginCredentials(String username, String password) {
        // Logic to securely save the username and hashed password for automatic login
        // maybe mongodb?
    }
}
