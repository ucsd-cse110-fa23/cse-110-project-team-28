package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import config.Config;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import model.Recipe;
import utilites.Logger;
import utilites.RecipeHelper;
import utilites.SceneHelper;
import utilites.URIBuilder;

public class RecipeDetailsController implements Initializable {

    @FXML
    private StackPane recipeImage;

    @FXML
    private Label recipeNameLabel;

    @FXML
    private Label mealTypeLabel;

    @FXML
    private Label ingredientsLabel;

    @FXML
    private Label stepsLabel;

    @FXML
    private Button shareRecipeButton;

    @FXML
    private Label recipeURLLabel;

    private Recipe recipe;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            if (recipe.getImageURL() == null)
                Logger.warn("Recipe image is null");

            recipeImage.setStyle(
                    "-fx-background-image: url('" + recipe.getImageURL() + "');");

            Rectangle clip = new Rectangle(recipeImage.getWidth(), recipeImage.getHeight());
            clip.setArcWidth(12);
            clip.setArcHeight(12);
            recipeImage.setClip(clip);

            recipeImage.widthProperty().addListener((observable, oldValue, newValue) -> {
                clip.setWidth(newValue.doubleValue());
            });

            recipeImage.heightProperty().addListener((observable, oldValue, newValue) -> {
                clip.setHeight(newValue.doubleValue());
            });
        });
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;

        recipeNameLabel.setText(recipe.getName());
        mealTypeLabel.setText(recipe.getMealType().substring(0, 1).toUpperCase() + recipe.getMealType().substring(1));
        ingredientsLabel.setText(recipe.getIngredients());
        stepsLabel.setText(recipe.getSteps());

        String recipeURL = constructRecipeURL(recipe);
        Logger.log("Recipe URL: " + recipeURL);
        // Set the URL in the label or text field
        recipeURLLabel.setText(recipeURL);
    }

    @FXML
    private void backButtonHandler() throws IOException {
        goHome();
    }

    private void goHome() throws IOException {
        SceneHelper.switchToMainScene();
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

    @FXML
    private void editRecipeButtonHandler() throws IOException {
        SceneHelper.switchToEditRecipeScene(recipe);
    }

    @FXML
    private void deleteRecipeButtonHandler() throws IOException {
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
}