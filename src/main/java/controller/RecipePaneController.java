package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Recipe;

public class RecipePaneController implements Initializable {

    @FXML
    private Label recipeName;

    @FXML
    private Button detailsButton;

    private Recipe recipe;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        setRecipeName(recipe.getName());
    }

    private void setRecipeName(String recipeName) {
        this.recipeName.setText(recipeName);
    }

    public void detailsButtonHandler() throws IOException {
        Scene scene = detailsButton.getScene();
        Stage stage = (Stage) scene.getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editRecipe.fxml"));
        Parent root = loader.load();
        EditRecipeController editRecipeController = loader.getController();

        editRecipeController.setRecipe(recipe);

        Scene newScene = new Scene(root, scene.getWidth(), scene.getHeight());

        stage.setScene(newScene);
        stage.show();
    }

}
