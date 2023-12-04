package controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.Recipe;
import utilites.ChatGPT;
import utilites.Logger;
import utilites.RecipeHelper;
import utilites.SceneHelper;

public class NewRecipeController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private Button saveRecipeButton;

    @FXML
    private VBox newRecipeCenter;

    public static final String ERROR_FLAG = "ERROR";
    private final String promptTemplate = "" + //
            "Generate a [mealType] recipe using the following ingredients only:[listOfIngredients]. " + //
            "Please include list of ingredients, preparation instructions, and numbered cooking steps. " + //
            "Place title of recipe on first line of your reponse. \n" + //
            "\n" + //
            "Meal Type: [mealType]\n" + //
            "Ingredients: [listOfIngredients]\n" + //
            "\n" + //
            "Recipe:";

    // meal type stuff added by dominic
    private String recipeName;
    private String mealType;
    private String ingredients;
    private String steps;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveRecipeButton.setDisable(true);

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
                        mealType = transcript;
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
                        ingredients = transcript;
                        String response = getRecipeSteps();
                        if (response.equals(ERROR_FLAG)) {
                            Logger.log("An error occurred while getting steps");
                        } else {
                            recipeName = getRecipeName(response);
                            steps = response;

                            saveRecipeButton.setDisable(false);
                        }
                    }
                }
            });

            newRecipeCenter.getChildren().addAll(mealTypePrompt, mealTypeRecorderPane, ingredientsPrompt,
                    ingredientsRecorderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRecipeSteps() {
        try {
            return ChatGPT.getGPTResponse(500, promptTemplate.replace("[mealType]",
                    mealType)
                    .replace("[listOfIngredients]", ingredients));
        } catch (IOException e) {
            e.printStackTrace();
            Logger.log("An I/O error occurred: " + e.getMessage());
            return ERROR_FLAG;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Logger.log("Invalid URI: Check file path.");
            return ERROR_FLAG;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Logger.log("InterruptedException");
            return ERROR_FLAG;
        }
    }

    public void backButtonHandler() throws IOException {
        SceneHelper.switchToMainScene();
    }

    private String getRecipeName(String steps) {
        String[] lines = steps.split("\n");
        return lines[0].trim();
    }

    public void saveRecipeButtonHandler() throws IOException {
        saveRecipe();
        SceneHelper.switchToMainScene();
    }

    /*
     * Saves recipe to main app list
     */
    private void saveRecipe() {
        Recipe recipe = new Recipe()
                .setName(recipeName)
                .setMealType(mealType)
                .setIngredients(ingredients)
                .setSteps(steps)
                .setImageUrl(""); // todo: implement this

        RecipeHelper.addRecipe(recipe);
    }
}
