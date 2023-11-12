package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class RecipePaneController implements Initializable {

    @FXML
    private Label recipeName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setRecipeName(String recipeName) {
        this.recipeName.setText(recipeName);
    }

}
