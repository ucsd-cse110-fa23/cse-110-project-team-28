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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Recipe;
import model.RecipeData;

public class EditRecipeController implements Initializable {

    @FXML
    private TextArea editRecipeTextArea;

    @FXML
    private Button saveRecipeButton;

    private Recipe recipe;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        editRecipeTextArea.setText(recipe.getSteps());
    }

    public void backButtonHandler() throws IOException {
        goHome();
    }

    private void goHome() throws IOException {
        Scene scene = editRecipeTextArea.getScene();
        Stage stage = (Stage) scene.getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Scene newScene = new Scene(root, scene.getWidth(), scene.getHeight());

        stage.setScene(newScene);
        stage.show();
    }

    public void saveRecipeButtonHandler() throws IOException {
        saveRecipe();
        goHome();
    }

    /*
     * Saves recipe to main app list
     */
    private void saveRecipe() {
        recipe.setSteps(editRecipeTextArea.getText());
    }

    public void deleteRecipeButtonHandler() throws IOException {
        deleteRecipe();
        goHome();
    }

    /*
     * Deletes recipe from main app list
     */
    private void deleteRecipe() {
        RecipeData.getInstance().deleteRecipe(recipe);
    }

}
