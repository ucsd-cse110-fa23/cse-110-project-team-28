package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Recipe;

public class RecipePaneControllerV2 implements Initializable {

    @FXML
    private Label recipeName;

    @FXML
    private Button detailsButton;

    @FXML
    private ImageView recipeImage;

    @FXML
    private StackPane recipePane;

    private Recipe recipe;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            recipePane.setStyle(
                    "-fx-background-image: url(\"" + recipe.getImageURL() + "\");");

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
