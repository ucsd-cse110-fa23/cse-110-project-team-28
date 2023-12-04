package controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import model.Recipe;
import utilites.RecipeHelper;
import utilites.SceneHelper;
import utilites.ChatGPT;

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
            "Generate a [mealType] recipe that is not [recipeName] using the following ingredients only:[listOfIngredients]. " + //
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

    private Recipe recipe;

    private String recipeName;
    private String mealType;
    private String ingredients;
    private String steps;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    //sets recipe and gets recipe steps
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        String response = getRecipeSteps(false);
        if(response.equals(ERROR_FLAG)){
            System.out.println("An error occurred while getting steps");
        } else{
            recipeName = getRecipeName(response);
            steps = response;
            recipe.setSteps(steps);//@TODO refactor maybe?
        }
        editRecipeTextArea.setText(recipe.getSteps());
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

    //@TODO figure out 
    public void saveRecipeButtonHandler() throws IOException {
        recipe.setSteps(editRecipeTextArea.getText());

        // save change to database
        System.out.println("Saving changes to recipe: " + recipe.getId());
        RecipeHelper.editRecipe(recipe);

        goHome();
    }

    public void regenerateRecipeButtonHandler() throws IOException {
        String response = getRecipeSteps(true);
        if(response.equals(ERROR_FLAG)){
            System.out.println("An error occurred while getting steps");
        } else{
            recipeName = getRecipeName(response);
            steps = response;
            recipe.setSteps(steps);//@TODO refactor maybe?
        }
        editRecipeTextArea.setText(recipe.getSteps());
    }
    
    /*
     * Gets recipe steps from gpt
     */
    private String getRecipeSteps(Boolean regenerate) {
        try {
            if(regenerate){
                return ChatGPT.getGPTResponse(500, promptRegenerateTemplate.replace("[mealType]",
                        recipe.getMealType())
                        .replace("[listOfIngredients]", recipe.getIngredients())
                        .replace("[recipeName]", recipeName));
            }else{
                return ChatGPT.getGPTResponse(500, promptTemplate.replace("[mealType]",
                        recipe.getMealType())
                        .replace("[listOfIngredients]", recipe.getIngredients()));
            }
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

    
}