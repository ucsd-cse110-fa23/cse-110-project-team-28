package controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import model.RecipeData;
import model.UserData;
import utilites.AuthHelper;
import utilites.AuthResponse;
import utilites.SceneHelper;

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
                    saveLogInCredentials(username, password);
                }
                UserData.setInstance(authResponse.getUserData());
                SceneHelper.switchToMainScene(usernameField.getScene());
            } else {
                setError(authResponse.getMessage());
            }
        } catch (IOException e) {
            showErrorPopup("Error connecting to the server. Please try again later.");
        } catch (Exception e) {
            showErrorPopup("An unexpected error occurred");
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

            AuthResponse authResponse = AuthHelper.login(username, password);

            if (authResponse.getSuccess()) {
                if (autoLoginCheckBox.isSelected()) {
                    saveLogInCredentials(username, password);
                }
                UserData.setInstance(authResponse.getUserData());
                RecipeData.getInstance().loadRecipes(username);
                SceneHelper.switchToMainScene(usernameField.getScene());
            } else {
                setError(authResponse.getMessage());
            }
        } catch (IOException e) {
            showErrorPopup("Error connecting to the server. Please try again later.");
        } catch (Exception e) {
            showErrorPopup("An unexpected error occurred");
        }
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

    private void saveLogInCredentials(String username, String password) {
        Preferences prefs = Preferences.userNodeForPackage(AuthenticationController.class);
        prefs.put("username", username);
        prefs.put("password", password);
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