
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import controller.MainController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Recipe;
import model.RecipeData;

public class CurrentInteractiveTests extends ApplicationTest {

    final int WIDTH = 800;
    final int HEIGHT = 500;

    private MainController controller;
    private Recipe originalRecipe;
    private RecipeData recipeData;

    private String TEST_RECIPE_FILE = "test_recipes.jsonl";

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        controller = loader.getController();

        primaryStage.setTitle("PantryPal");

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);

        primaryStage.show();
    }

    @Test
    public void testTitle() {
        FxAssert.verifyThat("#titleLabel", LabeledMatchers.hasText("PantryPal 2"));
    }

    @Test
    public void testListCreation() throws IOException {
        Platform.runLater(() -> {
            controller.addRecipe(new Recipe("Tasty Tests", "breakfast", "JUnit, TestFX, Gradle, and GitHub Actions",
                    "1. Mix well and pray your tests pass."));

            FxAssert.verifyThat(".recipePaneLabel", LabeledMatchers.hasText("Tasty Tests"));

        });
    }
/* 
    @Test
    public void deleteRecipe() throws IOException {
        Platform.runLater(() -> {
            Assert.assertEquals(controller.getRecipeList().getChildren().isEmpty(), true);

            controller.addRecipe(new Recipe("Tasty Tests", "breakfast", "JUnit, TestFX, Gradle, and GitHub Actions",
                    "1. Mix well and pray your tests pass."));

            sleep(100);

            Assert.assertEquals(false, controller.getRecipeList().getChildren().isEmpty());
            // Assert.assertEquals("Tasty Tests",
            // ((Label) ((StackPane)
            // controller.getRecipeList().getChildren().get(0)).getChildren().get(0))
            // .getText());
            FxAssert.verifyThat(".recipePaneLabel", LabeledMatchers.hasText("Tasty Tests"));

            Button recipePanelDetailsButton = lookup(".recipePaneDetailsButton").query();
            recipePanelDetailsButton.fire();

            Button deleteRecipeButton = lookup("#deleteRecipeButton").query();
            deleteRecipeButton.fire();

            // assert that recipedata is empty
            Assert.assertEquals(RecipeData.getInstance().getRecipes().isEmpty(), true);
        });
    }*/
/* 
    @Test
    public void testSaveAndLoadRecipes() throws Exception {
        // Setup
        RecipeData recipeData = new RecipeData();
        recipeData.setFileLocation(TEST_RECIPE_FILE);
        RecipeData.setInstance(recipeData);

        Recipe recipe = new Recipe("Test Recipe", "Dinner", "Ingredients", "Steps");
        // RecipeData.getInstance().getRecipes().clear(); // Clear existing recipes
        // edit: not necessary
        RecipeData.getInstance().addRecipe(recipe); // Add a test recipe

        // Save current recipes to file
        // MainController.setRecipeFile(TEST_RECIPE_FILE); // edit: this is handled at
        // the start of the test
        // MainController.saveAllRecipesToFile(); // Static call to save recipes
        RecipeData.getInstance().saveRecipes(recipe); // Instance call to save recipes

        // Verify save
        assertTrue("Recipe file should be created", Files.exists(Paths.get(TEST_RECIPE_FILE)));

        // Prepare for load test by clearing the recipes list
        RecipeData.getInstance().getRecipes().clear(); // Ensure the list is clear before loading

        // Create an instance of MainController to call loadRecipes
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setControllerFactory(c -> new MainController()); // Override controller creation
        // Parent root = loader.load(); // Load the FXML
        // MainController controller = loader.getController(); // Get the controller
        // instance

        // Set the file path for the MainController instance
        // controller.setRecipeFile(TEST_RECIPE_FILE); // Use instance method to set
        // file path

        // Load recipes using the instance method
        // controller.loadRecipes(); // Call instance method to load recipes

        RecipeData.getInstance().loadRecipes();

        // Verify load
        assertEquals("There should be one recipe after loading", 1, RecipeData.getInstance().getRecipes().size());
        Recipe loadedRecipe = RecipeData.getInstance().getRecipes().get(0);
        assertEquals("Loaded recipe name should match", "Test Recipe", loadedRecipe.getName());
    }

    @Test
    public void testEditRecipe() throws Exception {
        // Initialize the controller and recipe data
        controller = new MainController();
        recipeData = RecipeData.getInstance();
        recipeData.getRecipes().clear(); // Clear existing data

        // Add a recipe to be edited
        originalRecipe = new Recipe("Original Recipe", "Lunch", "Original ingredients", "Original steps");
        recipeData.addRecipe(originalRecipe);
        // Simulate user editing a recipe
        // For this example, let's say you're editing the name and ingredients
        String newRecipeName = "Edited Recipe";
        String newIngredients = "Edited ingredients";
        String newSteps = "Edited steps";

        // Retrieve the recipe to edit (normally you would do some sort of selection or
        // search here)
        Recipe recipeToEdit = recipeData.getRecipes().get(0);

        // Perform the edit (this logic would be in your edit handling method)
        recipeToEdit.setName(newRecipeName);
        recipeToEdit.setIngredients(newIngredients);
        recipeToEdit.setSteps(newSteps);

        // Assert that the changes have been made in the data model
        assertEquals("Recipe name should be edited", newRecipeName, recipeToEdit.getName());
        assertEquals("Ingredients should be edited", newIngredients, recipeToEdit.getIngredients());
        assertEquals("Steps should be edited", newSteps, recipeToEdit.getSteps());

        // Optionally, save the recipes and assert that the file has changed
        // controller.saveAllRecipesToFile(); // Assuming this method works as intended
        RecipeData.getInstance().saveRecipes(recipeToEdit);

        // Now clear the list and reload from file to ensure persistence
        recipeData.getRecipes().clear();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setControllerFactory(c -> new MainController()); // Override controller creation
        // Parent root = loader.load(); // Load the FXML
        // MainController controller = loader.getController(); // Get the controller
        // instance
        // controller.loadRecipes(); // Reload to reflect changes from file

        RecipeData.getInstance().loadRecipes();

        // Verify the first (and only) recipe is the edited one
        Recipe reloadedRecipe = recipeData.getRecipes().get(0);
        assertEquals("Reloaded recipe name should match edited name", newRecipeName, reloadedRecipe.getName());
        assertEquals("Reloaded ingredients should match edited ingredients", newIngredients,
                reloadedRecipe.getIngredients());
        assertEquals("Reloaded steps should match edited steps", newSteps, reloadedRecipe.getSteps());
    }*/

}