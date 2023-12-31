package controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletionException;

import model.UserData;
import server.AuthResponse;
import utilites.AuthHelper;
import utilites.AutoLoginHelper;
import utilites.ErrorPopupHelper;
import utilites.Logger;
import utilites.SceneHelper;
import java.net.ConnectException;

public class AuthenticationController implements Initializable {

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

        try {
            if (username.isEmpty() || password.isEmpty()) {
                setError("Please enter a username and password.");
                return;
            }

            AuthResponse authResponse = AuthHelper.signup(username, password);

            if (authResponse.getSuccess()) {

                if (autoLoginCheckBox.isSelected()) {
                    AutoLoginHelper.saveUserData(authResponse.getUserData());
                }

                UserData.setInstance(authResponse.getUserData());

                SceneHelper.switchToMainScene();
            } else {
                Logger.log("Signup failed. Server response: " + authResponse.toString());
                setError(authResponse.getMessage());
            }
        } catch (CompletionException e) {
            if (e.getCause() instanceof ConnectException) {
                Logger.error("Error connecting to the server");
                ErrorPopupHelper.showErrorPopup("Error connecting to the server. Please try again later.");
            } else {
                e.printStackTrace();
                ErrorPopupHelper.showErrorPopup("An unexpected error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorPopupHelper.showErrorPopup("An unexpected error occurred");
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

            Logger.log("Logging in with username: " + username + " and password: " + password);

            AuthResponse authResponse = AuthHelper.login(username, password);

            if (authResponse.getSuccess()) {
                Logger.log("Login successful");

                if (autoLoginCheckBox.isSelected()) {
                    AutoLoginHelper.saveUserData(authResponse.getUserData());
                }

                Logger.log("User data: " + authResponse.getUserData().toString());

                UserData.setInstance(authResponse.getUserData());

                SceneHelper.switchToMainScene();
            } else {
                Logger.log("Login failed. Server response: " + authResponse.toString());
                setError(authResponse.getMessage());
            }
        } catch (CompletionException e) {
            if (e.getCause() instanceof ConnectException) {
                Logger.error("Error connecting to the server");
                ErrorPopupHelper.showErrorPopup("Error connecting to the server. Please try again later.");
            } else {
                e.printStackTrace();
                ErrorPopupHelper.showErrorPopup("An unexpected error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorPopupHelper.showErrorPopup("An unexpected error occurred");
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        loginHandler();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        usernameField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        loginHandler();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}