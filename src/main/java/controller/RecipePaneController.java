package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import model.Recipe;
import utilites.Logger;
import utilites.SceneHelper;

public class RecipePaneController implements Initializable {

    @FXML
    private Label recipeName;

    @FXML
    private Button detailsButton;

    @FXML
    private ImageView recipeImage;

    @FXML
    private StackPane recipePane;

    @FXML
    private Label mealTypeLabel;

    private Recipe recipe;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            if (recipe.getImageURL() == null)
                Logger.warn("Recipe image is null");

            recipePane.setStyle(
                    "-fx-background-image: url('" + recipe.getImageURL() + "');");

            Rectangle clip = new Rectangle(recipePane.getWidth(), recipePane.getHeight());
            clip.setArcWidth(12);
            clip.setArcHeight(12);
            recipePane.setClip(clip);

            recipePane.widthProperty().addListener((observable, oldValue, newValue) -> {
                clip.setWidth(newValue.doubleValue());
            });

            recipePane.heightProperty().addListener((observable, oldValue, newValue) -> {
                clip.setHeight(newValue.doubleValue());
            });
        });
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;

        recipeName.setText(recipe.getName());
        // capitalize first letter of meal type
        mealTypeLabel.setText(recipe.getMealType().substring(0, 1).toUpperCase() + recipe.getMealType().substring(1));
    }

    public void detailsButtonHandler() throws IOException {
        SceneHelper.switchToRecipeDetailsScene(recipe);
    }

}
