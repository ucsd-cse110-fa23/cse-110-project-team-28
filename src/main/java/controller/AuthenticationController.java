package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.UserData;
import javafx.scene.control.Label;
import utilites.MongoDBHelper;
import utilites.SceneHelper;

import org.bson.Document;
import org.bson.types.ObjectId;

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

        // todo: add functionality
        // boolean autoLogin = autoLoginCheckBox.isSelected();

        if (MongoDBHelper.doesUsernameExist(username)) {
            setError("That username is taken.");
            return;
        }

        ObjectId objectId = MongoDBHelper.insertUser(username, password);

        UserData.getInstance().setObjectId(objectId);

        // todo: auto login

        SceneHelper.switchToMainScene(usernameField.getScene());
    }

    @FXML
    private void loginHandler() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            setError("Please enter a username and password.");
            return;
        }

        // todo: add functionality
        // boolean autoLogin = autoLoginCheckBox.isSelected();

        Document userDocument = MongoDBHelper.findUser(username, password);

        if (userDocument != null) {
            ObjectId objectId = userDocument.getObjectId("_id");
            UserData.getInstance().setObjectId(objectId);

            SceneHelper.switchToMainScene(usernameField.getScene());
        } else {
            setError("Incorrect username or password.");
        }
    }
}
