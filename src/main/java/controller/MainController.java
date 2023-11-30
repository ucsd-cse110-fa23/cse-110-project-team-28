package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.text.RandomStringGenerator;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.util.prefs.Preferences;
import model.Recipe;
import model.RecipeData;
import utilites.SceneHelper;

import java.util.ArrayList;

public class MainController implements Initializable {

    @FXML
    private VBox recipeList;

    @FXML
    private Button addRecipeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Platform.runLater(() -> {
        // this.reloadRecipeList();
        // });
    }

    public VBox getRecipeList() {
        return recipeList;
    }

    // private void reloadRecipeList() {
    // // clear recipeList
    // recipeList.getChildren().clear();

    // for (Recipe recipe : RecipeData.getInstance().getRecipes()) {
    // addRecipe(recipe);
    // }
    // }

    /**
     * Adds a recipe to the recipeList
     * 
     * @param recipeName the name of the recipe to add
     * @throws IOException
     */
    public void addRecipe(Recipe recipe) {
        // load recipePane.fxml and get its RecipeController
        // todo: using testing recipePane and RecipePaneControllerV2
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/recipePaneV2.fxml"));

        try {
            Parent recipePane = loader.load();

            RecipePaneControllerV2 recipePaneController = loader.getController();

            // set recipeName
            recipePaneController.setRecipe(recipe);

            // add recipePane to recipeList
            recipeList.getChildren().add(recipePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds recipes to the recipeList
     * 
     * @param recipeNames the names of the recipes to add
     * @throws IOException
     */
    public void addRecipes(ArrayList<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            addRecipe(recipe);
        }
    }

    /**
     * Clears the recipeList and adds the given recipes
     * 
     * @param recipes the recipes to add
     */
    public void setRecipes(ArrayList<Recipe> recipes) {
        recipeList.getChildren().clear();

        addRecipes(recipes);
    }

    public void addRecipeHandler() throws IOException {
        SceneHelper.switchToNewRecipeScene(addRecipeButton.getScene());
    }

    public void debugAddRecipeHandler() throws IOException {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        Recipe recipe = new Recipe(generator.generate(10), "Dinner",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                "https://picsum.photos/600/200");

        // todo: implement this
        // RecipeData.getInstance().addRecipe(recipe);

        // reloadRecipeList();
    }

    @FXML
    private void handleLogout() throws IOException {
        clearStoredCredentials();
        SceneHelper.switchToAuthenticationScene(recipeList.getScene());
    }

    private void clearStoredCredentials() {
        Preferences prefs = Preferences.userNodeForPackage(AuthenticationController.class);
        prefs.remove("username");
        prefs.remove("password");
    }

}
