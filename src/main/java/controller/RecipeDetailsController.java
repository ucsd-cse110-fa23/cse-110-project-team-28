package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import config.Config;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import model.Recipe;
import utilites.Logger;
import utilites.RecipeHelper;
import utilites.SceneHelper;
import utilites.URIBuilder;

public class RecipeDetailsController implements Initializable {

    @FXML
    private Label recipeNameLabel;

    @FXML
    private Label mealTypeLabel;

    @FXML
    private Label ingredientsLabel;

    @FXML
    private Label stepsLabel;

    @FXML
    private Button saveRecipeButton;

    @FXML
    private Button shareRecipeButton;

    @FXML
    private Label recipeURLLabel;

    private Recipe recipe;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;

        recipeNameLabel.setText(recipe.getName());
        mealTypeLabel.setText(recipe.getMealType());
        ingredientsLabel.setText(recipe.getIngredients());
        stepsLabel.setText(recipe.getSteps());

        String recipeURL = constructRecipeURL(recipe);
        Logger.log("RECIPE URL: " + recipeURL);
        // Set the URL in the label or text field
        recipeURLLabel.setText(recipeURL);
    }

    public void backButtonHandler() throws IOException {
        goHome();
    }

    private void goHome() throws IOException {
        SceneHelper.switchToMainScene();
    }

    @FXML
    private void editRecipeButtonHandler() throws IOException {
        // todo: implement navigation
        Logger.log("Edit recipe button clicked!");
    }

    public void deleteRecipeButtonHandler() throws IOException {
        RecipeHelper.deleteRecipe(recipe);
        goHome();
    }

    @FXML
    private void shareRecipeButtonHandler() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        content.putString(recipeURLLabel.getText());
        clipboard.setContent(content);

        Logger.log("Recipe URL copied to clipboard!");
    }

    private String constructRecipeURL(Recipe recipe) {
        try {
            return new URIBuilder()
                    .setHost(Config.getServerHostname())
                    .setPort(Config.getServerPort())
                    .setPath("/recipe/" + recipe.getId())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error constructing URL";
        }
    }
}