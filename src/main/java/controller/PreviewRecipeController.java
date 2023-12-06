package controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
    private Label recipeNameLabel;

    @FXML
    private Label mealTypeLabel;

    @FXML
    private Label ingredientsLabel;

    @FXML
    private Label stepsLabel;

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
        generateAndDisplayRecipe(false);
    }

    /**
     * a messy and hacky way of cleaning up the code
     * 
     * @param regenerate whether or not to regenerate the recipe
     */
    private void generateAndDisplayRecipe(boolean regenerate) {
        String response = getRecipeSteps(regenerate);

        if (response.equals(ERROR_FLAG)) {
            Logger.log("An error occurred while getting steps");
        } else {
            // clean up response
            response = response.trim();

            recipe.setName(extractRecipeName(response));
            recipe.setSteps(response);
        }

        recipeNameLabel.setText(recipe.getName());
        mealTypeLabel.setText(recipe.getMealType().substring(0, 1).toUpperCase() + recipe.getMealType().substring(1));
        ingredientsLabel.setText(recipe.getIngredients());
        stepsLabel.setText(recipe.getSteps());
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
            imageURL = DallE.generateImageForRecipe(recipe);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            Logger.log("Error getting image url"); // @TODO refactor to handle each exception separately
            e.printStackTrace();
        }

        return imageURL;
    }

    @FXML
    private void saveRecipeButtonHandler() throws IOException {

        String imageURL = getImageURL();

        recipe.setImageUrl(imageURL);

        Logger.log("Adding new recipe");

        RecipeHelper.addRecipe(recipe);

        goHome();
    }

    @FXML
    private void refreshRecipeButtonHandler() throws IOException {
        generateAndDisplayRecipe(true);
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

    private String extractRecipeName(String steps) {
        String[] lines = steps.split("\n");
        return lines[0].trim();
    }

}