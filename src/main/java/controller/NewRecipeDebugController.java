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
import utilites.Logger;
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
        Boolean status = saveRecipe();

        if (!status)
            return;

        SceneHelper.switchToMainScene();
    }

    /*
     * Saves recipe to main app list
     */
    private Boolean saveRecipe() {
        String mealType = mealTypeTextField.getText().toLowerCase().replaceAll("[^a-z]", "").trim();

        Logger.log("Parsed meal type: " + mealType);

        // validate meal type
        switch (mealType) {
            case "breakfast":
            case "lunch":
            case "dinner":
                break;
            default:
                Logger.warn("Invalid meal type: " + mealTypeTextField.getText());
                return false;
        }

        if (nameTextField.getText().isEmpty()) {
            Logger.warn("Recipe name is empty");
            return false;
        }

        if (ingredientsTextField.getText().isEmpty()) {
            Logger.warn("Ingredients are empty");
            return false;
        }

        if (stepsTextField.getText().isEmpty()) {
            Logger.warn("Steps are empty");
            return false;
        }

        Recipe recipe = new Recipe()
                .setName(nameTextField.getText())
                .setMealType(mealTypeTextField.getText())
                .setIngredients(ingredientsTextField.getText())
                .setSteps(stepsTextField.getText());
        // .setImageUrl(imageURLTextField.getText());

        RecipeHelper.addRecipe(recipe);

        return true;
    }
}
