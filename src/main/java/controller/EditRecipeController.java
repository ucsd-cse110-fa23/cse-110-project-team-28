package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.Recipe;
import utilites.Logger;
import utilites.RecipeHelper;
import utilites.SceneHelper;

public class EditRecipeController implements Initializable {

    @FXML
    private Label recipeNameLabel;

    @FXML
    private Label mealTypeLabel;

    @FXML
    private Label ingredientsLabel;

    @FXML
    private TextArea stepsTextArea;

    @FXML
    private Button saveRecipeButton;

    private Recipe recipe;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;

        recipeNameLabel.setText(recipe.getName());
        mealTypeLabel.setText(recipe.getMealType());
        ingredientsLabel.setText(recipe.getIngredients());
        stepsTextArea.setText(recipe.getSteps());
    }

    @FXML
    private void backButtonHandler() throws IOException {
        SceneHelper.switchToRecipeDetailsScene(recipe);
    }

    @FXML
    private void saveRecipeButtonHandler() throws IOException {
        recipe.setSteps(stepsTextArea.getText());

        // save change to database
        Logger.log("Saving changes to recipe: " + recipe.getId());

        RecipeHelper.editRecipe(recipe);

        // get updated recipe and switch to details scene
        Recipe editedRecipe = RecipeHelper.getRecipe(recipe.getId());

        SceneHelper.switchToRecipeDetailsScene(editedRecipe);
    }
}