package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import model.Recipe;
import model.RecipeData;
import utilites.SceneHelper;

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
        SceneHelper.switchToMainScene(editRecipeTextArea.getScene());
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

        // save change to file
        // todo: implement this
        // RecipeData.getInstance().saveRecipes();
    }

    public void deleteRecipeButtonHandler() throws IOException {
        deleteRecipe();
        goHome();
    }

    /*
     * Deletes recipe from main app list
     */
    private void deleteRecipe() {
        // todo: implement this
        // RecipeData.getInstance().deleteRecipe(recipe);
    }

}
