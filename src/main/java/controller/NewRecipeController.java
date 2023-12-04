package controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Recipe;
import utilites.ChatGPT;
import utilites.RecipeHelper;

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
                        System.out.println("An error occurred while getting meal type");
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
                        System.out.println("An error occurred while getting ingredients");
                    } else {
                        ingredients = transcript;
                        String response = getRecipeSteps();
                        if (response.equals(ERROR_FLAG)) {
                            System.out.println("An error occurred while getting steps");
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

    public void backButtonHandler() throws IOException {
        goHome();
    }

    private void goHome() throws IOException {
        Scene scene = backButton.getScene();
        Stage stage = (Stage) scene.getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Scene newScene = new Scene(root, scene.getWidth(), scene.getHeight());

        stage.setScene(newScene);
        stage.show();
    }

    private String getRecipeSteps() {
        try {
            return ChatGPT.getGPTResponse(500, promptTemplate.replace("[mealType]",
                    mealType)
                    .replace("[listOfIngredients]", ingredients));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An I/O error occurred: " + e.getMessage());
            return ERROR_FLAG;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Invalid URI: Check file path.");
            return ERROR_FLAG;
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("InterruptedException");
            return ERROR_FLAG;
        }
    }

    private String getRecipeName(String steps) {
        String[] lines = steps.split("\n");
        return lines[0].trim();
    }

    public void saveRecipeButtonHandler() throws IOException {
        saveRecipe();
        goHome();
    }

    /*
     * Saves recipe to main app list
     */
    private void saveRecipe() {
        Recipe recipe = new Recipe();
        recipe.setName(recipeName);
        recipe.setMealType(mealType);
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);

        // todo: implement this
        // RecipeData.getInstance().addRecipe(recipe);
        RecipeHelper.addRecipe(recipe);
    }
}
