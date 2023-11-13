
import org.junit.Assert;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Recipe;
import model.RecipeData;

public class InteractiveTests extends ApplicationTest {

    final int WIDTH = 800;
    final int HEIGHT = 500;

    private Recipe originalRecipe;
    private RecipeData recipeData;

    private String TEST_RECIPE_FILE = "test_recipes.json";

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        loader.getController();

        primaryStage.setTitle("PantryPal");

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);

        primaryStage.show();
    }

    @Before
    public void setUp() throws Exception {
        // clear the recipe list before each test
        RecipeData.getInstance().getRecipes().clear();
        // note: not removing the observer will cause fx thread errors
        RecipeData.getInstance().removeObserver();

        // clear the recipe file before each test
        Files.deleteIfExists(Paths.get(TEST_RECIPE_FILE));
    }

    @Test
    public void sanityTest() {
        Assert.assertEquals("PantryPal", lookup("#titleLabel").queryAs(Label.class).getText());
    }

    @Test
    public void testSaveAndLoadRecipes() throws Exception {
        // Setup
        Recipe recipe = new Recipe("Test Recipe", "Dinner", "Ingredients", "Steps");
        RecipeData.getInstance().getRecipes().clear(); // Clear existing recipes
        RecipeData.getInstance().addRecipe(recipe); // Add a test recipe

        RecipeData.getInstance().saveRecipes(TEST_RECIPE_FILE);

        // Verify save
        assertTrue("Recipe file should be created", Files.exists(Paths.get(TEST_RECIPE_FILE)));

        // Prepare for load test by clearing the recipes list
        RecipeData.getInstance().getRecipes().clear(); // Ensure the list is clear before loading

        // Load recipes using the instance method
        RecipeData.getInstance().loadRecipes(TEST_RECIPE_FILE);

        // Verify load
        assertEquals("There should be one recipe after loading", 1, RecipeData.getInstance().getRecipes().size());
        Recipe loadedRecipe = RecipeData.getInstance().getRecipes().get(0);
        assertEquals("Loaded recipe name should match", "Test Recipe", loadedRecipe.getName());
    }

    @Test
    public void testEditRecipe() throws Exception {
        new MainController();
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

        // Retrieve the recipe to edit (normally you would do some sort of selection or search here)
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
        RecipeData.getInstance().saveRecipes(TEST_RECIPE_FILE);

        // Now clear the list and reload from file to ensure persistence
        recipeData.getRecipes().clear();

        // Load recipes using the instance method
        RecipeData.getInstance().loadRecipes(TEST_RECIPE_FILE);

        // Verify the first (and only) recipe is the edited one
        Recipe reloadedRecipe = recipeData.getRecipes().get(0);
        assertEquals("Reloaded recipe name should match edited name", newRecipeName, reloadedRecipe.getName());
        assertEquals("Reloaded ingredients should match edited ingredients", newIngredients, reloadedRecipe.getIngredients());
        assertEquals("Reloaded steps should match edited steps", newSteps, reloadedRecipe.getSteps());
    }

}