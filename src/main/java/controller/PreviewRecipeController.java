package controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import model.Recipe;
import utilites.RecipeHelper;
import utilites.SceneHelper;
import utilites.ChatGPT;
import utilites.DallE;
import utilites.Logger;

public class PreviewRecipeController implements Initializable {

    public static final String ERROR_FLAG = "ERROR";
    private final String promptTemplate = "" + //
            "Generate a [mealType] recipe using the following ingredients only:[listOfIngredients]. " + //
            "Please include list of ingredients, preparation instructions, and numbered cooking steps. " + //
            "Place title of recipe on first line of your response. \n" + //
            "\n" + //
            "Meal Type: [mealType]\n" + //
            "Ingredients: [listOfIngredients]\n" + //
            "\n" + //
            "Recipe:";
    private final String promptRegenerateTemplate = "" + //
            "Generate a [mealType] recipe that is not [recipeName] using the following ingredients only:[listOfIngredients]. "
            + //
            "Please include list of ingredients, preparation instructions, and numbered cooking steps. " + //
            "Place title of recipe on first line of your response. \n" + //
            "\n" + //
            "Meal Type: [mealType]\n" + //
            "Ingredients: [listOfIngredients]\n" + //
            "\n" + //
            "Recipe:";

    @FXML
    private TextArea editRecipeTextArea;

    @FXML
    private Button saveRecipeButton;

    @FXML
    private Button regenerateRecipeButton;

    Recipe recipe;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /*
     * Sets the recipe and asks gpt for steps
     * 
     * @TODO refactor
     */
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;

        String response = getRecipeSteps(false);

        if (response.equals(ERROR_FLAG)) {
            Logger.log("An error occurred while getting steps");
        } else {
            recipe.setName(getRecipeName(response));
            recipe.setSteps(response);
        }

        editRecipeTextArea.setText(response);
    }

    public void backButtonHandler() throws IOException {
        goBack();
    }

    private void goBack() throws IOException {
        SceneHelper.switchToNewRecipeScene();
    }

    private void goHome() throws IOException {
        SceneHelper.switchToMainScene();
    }

    /*
     * Gets an image url in Base64 format from DallE
     */
    private String getImageURL() {
        String imageURL = "";
        try {
            imageURL = DallE.generateImageURL(recipe.getName());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            Logger.log("Error getting image url"); // @TODO refactor to handle each exception separately
            e.printStackTrace();
        }
        return imageURL;
    }

    // @TODO figure out
    public void saveRecipeButtonHandler() throws IOException {

        String imageURL = getImageURL();

        recipe.setImageUrl(imageURL);

        Logger.log("Adding new recipe");

        RecipeHelper.addRecipe(recipe);

        goHome();
    }

    public void regenerateRecipeButtonHandler() throws IOException {
        String response = getRecipeSteps(true);
        if (response.equals(ERROR_FLAG)) {
            Logger.log("An error occurred while regenerating steps");
        } else {
            recipe.setName(getRecipeName(response));
            recipe.setSteps(response);
        }
        editRecipeTextArea.setText(response);
    }

    /*
     * Gets recipe steps from gpt
     */
    private String getRecipeSteps(Boolean regenerate) {
        try {
            if (regenerate) {
                return ChatGPT.getGPTResponse(500, promptRegenerateTemplate.replace("[mealType]",
                        recipe.getMealType())
                        .replace("[listOfIngredients]", recipe.getIngredients())
                        .replace("[recipeName]", recipe.getName()));
            } else {
                return ChatGPT.getGPTResponse(500, promptTemplate.replace("[mealType]",
                        recipe.getMealType())
                        .replace("[listOfIngredients]", recipe.getIngredients()));
            }
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

    private String getRecipeName(String steps) {
        String[] lines = steps.split("\n");
        return lines[0].trim();
    }

}