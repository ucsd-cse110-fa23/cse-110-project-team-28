package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.text.RandomStringGenerator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Observer;
import model.Recipe;
import model.RecipeData;

import java.util.ArrayList;

public class MainController implements Initializable, Observer {

    @FXML
    private VBox recipeList;

    @FXML
    private Button addRecipeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.updateRecipeList();

        RecipeData.getInstance().setObserver(this);
    }

    public VBox getRecipeList() {
        return recipeList;
    }

    private void updateRecipeList() {
        // clear recipeList
        recipeList.getChildren().clear();

        RecipeData recipeData = RecipeData.getInstance();

        for (Recipe recipe : recipeData.getRecipes()) {
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/recipePane.fxml"));

        try {
            GridPane recipePane = loader.load();
            RecipePaneController recipePaneController = loader.getController();

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
     * @param recipes the recipes to add
     */
    public void setRecipes(ArrayList<Recipe> recipes) {
        recipeList.getChildren().clear();

        addRecipes(recipes);
    }

    public void addRecipeHandler() throws IOException {
        Scene scene = addRecipeButton.getScene();
        Stage stage = (Stage) scene.getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/newRecipe.fxml"));
        Scene newScene = new Scene(root, scene.getWidth(), scene.getHeight());

        stage.setScene(newScene);
    }

    public void debugAddRecipeHandler() throws IOException {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        Recipe recipe = new Recipe(generator.generate(10), "Dinner",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        RecipeData.getInstance().addRecipe(recipe);

        updateRecipeList();
    }

    @Override
    public void update() {
        updateRecipeList();
    }
}
