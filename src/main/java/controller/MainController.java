package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.text.RandomStringGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Recipe;
import model.RecipeData;

public class MainController implements Initializable, RecipeUpdateListener {

    @FXML
    private VBox recipeList;

    @FXML
    private Button addRecipeButton;

    private static String recipeFilePath = "recipes.jsonl";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //loadRecipes();
        this.updateRecipeList();
    }
    
    @Override
    public void onRecipeUpdated() {
        updateRecipeList();
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
        GridPane recipePane;
        try {
            recipePane = loader.load();

            RecipePaneController recipePaneController = loader.getController();

            // set recipeName
            recipePaneController.setRecipe(recipe);

            // add recipePane to recipeList
            recipeList.getChildren().add(recipePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveAllRecipesToFile();
    }

    /**
     * Adds recipes to the recipeList
     * 
     * @param recipeNames the names of the recipes to add
     * @throws IOException
     */
    public void addRecipes(Recipe[] recipes) throws IOException {
        for (Recipe recipe : recipes) {
            addRecipe(recipe);
        }
    }

    public void addRecipeHandler() throws IOException {
        Scene scene = addRecipeButton.getScene();
        Stage stage = (Stage) scene.getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/newRecipe.fxml"));
        Scene newScene = new Scene(root, scene.getWidth(), scene.getHeight());

        stage.setScene(newScene);
        stage.show();
    }

    public void debugAddRecipeHandler() throws IOException {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        Recipe recipe = new Recipe(generator.generate(10), "Dinner",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        RecipeData.getInstance().addRecipe(recipe);

        updateRecipeList();
    }

    public void onSaveRecipesAction(ActionEvent event) {
        saveAllRecipesToFile();
    }

    public void onShowView() {
        updateRecipeList(); // Make sure this method refreshes the view correctly.
    }

    // Method to save recipes to a file
    public static void saveAllRecipesToFile() {
        // Get all recipes from RecipeData
        RecipeData recipeData = RecipeData.getInstance();
        // Convert the entire list to JSON and write to the file
        try (FileWriter fileWriter = new FileWriter(recipeFilePath, false)) { // false to overwrite
            for (Recipe recipe : recipeData.getRecipes()) {
                String json = recipe.toJson();
                fileWriter.write(json + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to change the file path, mainly for testing purposes
    public static void setRecipeFile(String filePath) {
        recipeFilePath = filePath; //this.
    }

    // Method to load recipes from a file
    public void loadRecipes() {
        RecipeData.getInstance().getRecipes().clear(); // Clear the current list before loading
        File file = new File(recipeFilePath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Recipe recipe = Recipe.fromJson(line);
                    if (recipe != null) {
                        RecipeData.getInstance().addRecipe(recipe);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        updateRecipeList();
    }
}
