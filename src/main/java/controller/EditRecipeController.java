package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import model.Recipe;
import model.RecipeData;
import utilites.RecipeHelper;
import utilites.SceneHelper;

public class EditRecipeController implements Initializable {

    @FXML
    private TextArea editRecipeTextArea;

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
        editRecipeTextArea.setText(recipe.getSteps());
        String recipeURL = constructRecipeURL(recipe);
        System.out.println("RECIPE URL: " + recipeURL);
        // Set the URL in the label or text field
        recipeURLLabel.setText(recipeURL);
    }

    public void backButtonHandler() throws IOException {
        goHome();
    }

    private void goHome() throws IOException {
        SceneHelper.switchToMainScene();
    }

    public void saveRecipeButtonHandler() throws IOException {
        recipe.setSteps(editRecipeTextArea.getText());

        // save change to database
        System.out.println("Saving changes to recipe: " + recipe.getId());
        RecipeHelper.editRecipe(recipe);

        goHome();
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
        System.out.println("Recipe URL copied to clipboard!");
    }

    private String constructRecipeURL(Recipe recipe) {
        try {
            return "http://localhost:8000/api/recipes?_id=" + recipe.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error constructing URL";
        }
    }
}