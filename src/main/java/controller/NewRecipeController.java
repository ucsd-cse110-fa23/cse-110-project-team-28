package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.Recipe;
import utilites.SceneHelper;
import utilites.Logger;

public class NewRecipeController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private Button generateRecipeButton;

    @FXML
    private VBox newRecipeCenter;

    public static final String ERROR_FLAG = "ERROR";

    private Recipe recipe;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // generateRecipeButton.setDisable(true);

        this.recipe = new Recipe();

        Label mealTypePrompt = new Label();
        mealTypePrompt.setText("Meal Type");
        mealTypePrompt.getStyleClass().add("subheading");

        Label ingredientsPrompt = new Label();
        ingredientsPrompt.setText("Ingredients");
        ingredientsPrompt.getStyleClass().add("subheading");

        Button saveButton = new Button();
        saveButton.setText("Save Recipe");

        FXMLLoader mealTypeRecorderLoader = new FXMLLoader(getClass().getResource("/fxml/recorder.fxml"));
        FXMLLoader ingredientsRecorderLoader = new FXMLLoader(getClass().getResource("/fxml/recorder.fxml"));

        try {
            VBox mealTypeRecorderPane = mealTypeRecorderLoader.load();
            RecorderController mealTypeRecorderController = mealTypeRecorderLoader.getController();

            mealTypeRecorderController.setFileName("mealtype.wav");
            mealTypeRecorderController.setTranscriptionCallback(new RecorderController.TranscriptionCallback() {
                @Override
                public void onTranscriptionComplete(String transcript) {
                    if (transcript.equals(ERROR_FLAG)) {
                        Logger.log("An error occurred while getting meal type");
                    } else {
                        recipe.setMealType(transcript);
                    }
                }
            });

            VBox ingredientsRecorderPane = ingredientsRecorderLoader.load();
            RecorderController ingredientsRecorderController = ingredientsRecorderLoader.getController();

            ingredientsRecorderController.setFileName("ingredients.wav");
            ingredientsRecorderController.setTranscriptionCallback(new RecorderController.TranscriptionCallback() {
                @Override
                public void onTranscriptionComplete(String transcript) {
                    if (transcript.equals(ERROR_FLAG)) {
                        Logger.log("An error occurred while getting ingredients");
                    } else {
                        recipe.setIngredients(transcript);
                    }
                }
            });

            newRecipeCenter.getChildren().addAll(mealTypePrompt, mealTypeRecorderPane, ingredientsPrompt,
                    ingredientsRecorderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backButtonHandler() throws IOException {
        SceneHelper.switchToMainScene();
    }

    @FXML
    private void generateRecipeButtonHandler() throws IOException {
        Logger.log("Creating a " + recipe.getMealType() + "recipe with the following ingredients: "
                + recipe.getIngredients());
        SceneHelper.switchToRecipePreviewScene(recipe);
    }
}
