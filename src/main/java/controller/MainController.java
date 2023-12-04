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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import java.util.prefs.Preferences;
import model.Recipe;
import model.RecipeData;
import model.UserData;
import utilites.RecipeHelper;
import utilites.SceneHelper;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

public class MainController implements Initializable {

    @FXML
    private VBox recipeList;

    @FXML
    private Button addRecipeButton;

    @FXML
    private ToggleButton toggleBreakfast;

    @FXML
    private ToggleButton toggleLunch;

    @FXML
    private ToggleButton toggleDinner;

    @FXML
    private ComboBox<String> sortComboBox;

    private List<Recipe> recipes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            System.out.println("Loading user recipes");

            loadRecipes();

            System.out.println(recipes.size() + " recipes loaded");

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
    private void addRecipe(Recipe recipe) {
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
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        Recipe recipe = new Recipe()
                .setName(generator.generate(10))
                .setMealType("Dinner")
                .setIngredients(
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
                .setSteps(
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
                .setImageUrl("https://picsum.photos/600/200");

        RecipeHelper.addRecipe(recipe);

        // todo: implement this
        // RecipeData.getInstance().addRecipe(recipe);

        // re-load recipes
        loadRecipes();
    }

    @FXML
    private void handleLogout() throws IOException {
        clearStoredCredentials();
        SceneHelper.switchToAuthenticationScene();
    }

    private void clearStoredCredentials() {
        Preferences prefs = Preferences.userNodeForPackage(AuthenticationController.class);
        prefs.remove("username");
        prefs.remove("password");
    }

    private void applySortAndFilter() {
        List<Recipe> filteredRecipes = new ArrayList<>();

        boolean filterBreakfast = toggleBreakfast.isSelected();
        boolean filterLunch = toggleLunch.isSelected();
        boolean filterDinner = toggleDinner.isSelected();

        System.out.println("Filter breakfast: " + filterBreakfast);
        System.out.println("Filter lunch: " + filterLunch);
        System.out.println("Filter dinner: " + filterDinner);

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

        System.out.println("Selected sort: " + selectedSort);

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

        System.out.println("Filtered recipes: " + filteredRecipes);

        setRecipes(filteredRecipes);
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
