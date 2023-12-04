package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import java.util.prefs.Preferences;
import model.Recipe;
import model.UserData;
import utilites.RecipeHelper;
import utilites.SceneHelper;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import utilites.Logger;

public class MainController implements Initializable {

    @FXML
    private VBox recipeList;

    @FXML
    private Button addRecipeButton;

    @FXML
    public ToggleButton toggleBreakfast;

    @FXML
    public ToggleButton toggleLunch;

    @FXML
    public ToggleButton toggleDinner;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label recipeCountLabel;

    @FXML
    public ComboBox<String> sortComboBox;

    public List<Recipe> recipes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            Logger.log("Loading user recipes");

            loadRecipes();

            usernameLabel.setText(UserData.getInstance().getUsername());
            recipeCountLabel.setText(recipes.size() + " recipes (" + recipes.size() + " shown)");

            Logger.log(recipes.size() + " recipes loaded");

            sortComboBox.setValue("Newest to Oldest");
        });
    }

    private void loadRecipes() {
        recipes = RecipeHelper.getUserRecipes(UserData.getInstance().getUserId());
        setRecipes(recipes);
    }

    /**
     * Clears the recipeList and adds the given recipes
     * 
     * @param recipes the recipes to add
     */
    private void setRecipes(List<Recipe> recipes) {
        recipeList.getChildren().clear();
        addRecipes(recipes);
    }

    /**
     * Adds recipes to the recipeList
     * 
     * @param recipeNames the names of the recipes to add
     * @throws IOException
     */
    private void addRecipes(List<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            addRecipe(recipe);
        }
    }

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

    @FXML
    private void addRecipeHandler() throws IOException {
        SceneHelper.switchToNewRecipeScene();
    }

    @FXML
    private void debugAddRecipeHandler() throws IOException {
        SceneHelper.switchToNewRecipeDebugScene();
    }

    @FXML
    private void logoutButtonHandler() throws IOException {
        clearStoredCredentials();
        SceneHelper.switchToAuthenticationScene();
    }

    private void clearStoredCredentials() {
        Preferences prefs = Preferences.userNodeForPackage(AuthenticationController.class);
        prefs.remove("username");
        prefs.remove("password");
    }

    public List<Recipe> applySortAndFilter() {
        List<Recipe> filteredRecipes = new ArrayList<>();

        boolean filterBreakfast = toggleBreakfast.isSelected();
        boolean filterLunch = toggleLunch.isSelected();
        boolean filterDinner = toggleDinner.isSelected();

        Logger.log("Filter breakfast: " + filterBreakfast);
        Logger.log("Filter lunch: " + filterLunch);
        Logger.log("Filter dinner: " + filterDinner);

        // Check if none are selected
        if (!filterBreakfast && !filterLunch && !filterDinner) {
            filteredRecipes = recipes;
        } else {
            for (Recipe recipe : recipes) {
                if ((filterBreakfast && recipe.getMealType().equals("Breakfast")) ||
                        (filterLunch && recipe.getMealType().equals("Lunch")) ||
                        (filterDinner && recipe.getMealType().equals("Dinner"))) {
                    filteredRecipes.add(recipe);
                }
            }
        }

        String selectedSort = sortComboBox.getValue();

        Logger.log("Selected sort: " + selectedSort);

        switch (selectedSort) {
            case "A-Z":
                filteredRecipes.sort(Comparator.comparing(Recipe::getName));
                break;
            case "Z-A":
                filteredRecipes.sort(Comparator.comparing(Recipe::getName).reversed());
                break;
            case "Oldest to Newest":
                Collections.reverse(filteredRecipes);
                break;
            case "Newest to Oldest":
                // do nothing
                break;
        }

        recipeCountLabel.setText(recipes.size() + " recipes (" + filteredRecipes.size() + " shown)");

        setRecipes(filteredRecipes);

        return filteredRecipes;
    }

    @FXML
    private void filterHandler() {
        applySortAndFilter();
    }

    @FXML
    private void sortHandler() {
        applySortAndFilter();
    }

}
