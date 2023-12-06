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
    public static final String ERROR_FLAG = "ERROR";

    @FXML
    private Button backButton;

    @FXML
    private Button generateRecipeButton;

    @FXML
    private VBox newRecipeCenter;

    private Recipe recipe;
    private RecorderController mealTypeRecorderController;
    private RecorderController ingredientsRecorderController;

    private boolean isMealTypeValid = false;
    private boolean isIngredientsValid = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateRecipeButton.setDisable(true);

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
            mealTypeRecorderController = mealTypeRecorderLoader.getController();

            mealTypeRecorderController.setFileName("mealtype.wav");
            mealTypeRecorderController.setTranscriptionCallback(new RecorderController.TranscriptionCallback() {
                @Override
                public String onTranscriptionComplete(String mealTypeTranscript) {

                    mealTypeTranscript = mealTypeTranscript.toLowerCase().replaceAll("[^a-z]", "").trim();

                    // validate meal type
                    switch (mealTypeTranscript) {
                        case "breakfast":
                        case "lunch":
                        case "dinner":
                            break;
                        default:
                            isMealTypeValid = false;

                            updateGenerateRecipeButton();

                            return null; // not a valid meal type
                    }

                    isMealTypeValid = true;

                    recipe.setMealType(mealTypeTranscript);

                    updateGenerateRecipeButton();

                    return mealTypeTranscript;
                }
            });

            VBox ingredientsRecorderPane = ingredientsRecorderLoader.load();
            ingredientsRecorderController = ingredientsRecorderLoader.getController();

            ingredientsRecorderController.setFileName("ingredients.wav");
            ingredientsRecorderController.setTranscriptionCallback(new RecorderController.TranscriptionCallback() {
                @Override
                public String onTranscriptionComplete(String ingredientsTranscript) {
                    if (ingredientsTranscript.equals(ERROR_FLAG)) {
                        Logger.log("An error occurred while getting ingredients");
                        isIngredientsValid = false;

                        updateGenerateRecipeButton();

                        return ingredientsTranscript;
                    }

                    recipe.setIngredients(ingredientsTranscript);

                    isIngredientsValid = true;

                    updateGenerateRecipeButton();

                    return ingredientsTranscript;
                }
            });

            newRecipeCenter.getChildren().addAll(mealTypePrompt, mealTypeRecorderPane, ingredientsPrompt,
                    ingredientsRecorderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateGenerateRecipeButton() {
        if (isMealTypeValid && isIngredientsValid) {
            generateRecipeButton.setDisable(false);
        } else {
            generateRecipeButton.setDisable(true);
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
        SceneHelper.switchToRecipePreviewV2Scene(recipe);
    }
}
