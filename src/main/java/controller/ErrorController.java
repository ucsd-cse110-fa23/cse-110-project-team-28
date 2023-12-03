package controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ErrorController {

    @FXML
    private Text errorMessage;

    public void setErrorMessage(String message) {
        errorMessage.setText(message);
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) errorMessage.getScene().getWindow();
        stage.close();
    }
}
