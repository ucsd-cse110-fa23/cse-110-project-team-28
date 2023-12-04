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

    private String mealType;
    private String ingredients;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //saveRecipeButton.setDisable(true);

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
        SceneHelper.switchToMainScene();
    }


    
    public void generateRecipeButtonHandler() throws IOException {
        goToPreview();
    }

    

    //TODO: maybe refactor and include in SceneHelper
    /*
     * switch to preview window
     */
    private void goToPreview() throws IOException{
        Scene scene = generateRecipeButton.getScene();
        Stage stage = (Stage) scene.getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/previewRecipe.fxml"));
        Parent root = loader.load();
        PreviewRecipeController previewRecipeController = loader.getController();


        Recipe newRecipe = new Recipe();
        newRecipe.setIngredients(ingredients);
        newRecipe.setMealType(mealType);
        previewRecipeController.setRecipe(ingredients, mealType);

        Scene newScene = new Scene(root, scene.getWidth(), scene.getHeight());

        stage.setScene(newScene);
        stage.show();
    }
}
