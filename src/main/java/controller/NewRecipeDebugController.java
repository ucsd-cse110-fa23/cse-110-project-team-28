package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Recipe;
import utilites.RecipeHelper;
import utilites.SceneHelper;

public class NewRecipeDebugController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField mealTypeTextField;

    @FXML
    private TextField ingredientsTextField;

    @FXML
    private TextField stepsTextField;

    @FXML
    private TextField imageURLTextField;

    @FXML
    private Button saveRecipeButton;

    @FXML
    private VBox newRecipeCenter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveRecipeButton.setDisable(false);
    }

    @FXML
    private void backButtonHandler() throws IOException {
        SceneHelper.switchToMainScene();
    }

    @FXML
    private void saveRecipeButtonHandler() throws IOException {
        saveRecipe();
        SceneHelper.switchToMainScene();
    }

    /*
     * Saves recipe to main app list
     */
    private void saveRecipe() {
        Recipe recipe = new Recipe()
                .setName(nameTextField.getText())
                .setMealType(mealTypeTextField.getText())
                .setIngredients(ingredientsTextField.getText())
                .setSteps(stepsTextField.getText());
                //.setImageUrl(imageURLTextField.getText());

        RecipeHelper.addRecipe(recipe);
    }
}
