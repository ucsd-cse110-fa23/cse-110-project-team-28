package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.UserData;
import javafx.scene.control.Label;
import utilites.AuthHelper;
import utilites.AuthResponse;
import utilites.MongoDBHelper;
import utilites.SceneHelper;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import com.google.gson.JsonObject;

public class AuthenticationController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox autoLoginCheckBox;

    @FXML
    private Label errorLabel;

    private void setError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @FXML
    private void signUpHandler() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            setError("Please enter a username and password.");
            return;
        }

        AuthResponse authResponse = AuthHelper.signup(username, password);

        if (authResponse.getSuccess()) {
            UserData.setInstance(authResponse.getUserData());

            // todo: add functionality
            // boolean autoLogin = autoLoginCheckBox.isSelected();

            SceneHelper.switchToMainScene(usernameField.getScene());
        } else {
            setError(authResponse.getMessage());
            return;
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

        AuthResponse authResponse = AuthHelper.login(username, password);

        if (authResponse.getSuccess()) {
            UserData.setInstance(authResponse.getUserData());

            // todo: add functionality
            // boolean autoLogin = autoLoginCheckBox.isSelected();

            SceneHelper.switchToMainScene(usernameField.getScene());
        } else {
            setError(authResponse.getMessage());
            return;
        }
    }
}
