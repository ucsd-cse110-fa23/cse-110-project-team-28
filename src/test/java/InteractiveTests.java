
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.testfx.framework.junit.ApplicationTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Recipe;
import model.UserData;
import utilites.RecipeHelper;

public class InteractiveTests extends ApplicationTest {

    final int WIDTH = 800;
    final int HEIGHT = 500;

    private String TEST_FILE = "test_recipes.json";

    MockedStatic<RecipeHelper> recipeHelper;

    @Override
    public void start(Stage primaryStage) throws Exception {
        UserData.setInstance(mock(UserData.class));
        when(UserData.getInstance().getUsername()).thenReturn("test_username");
        when(UserData.getInstance().getUserId()).thenReturn("test_id");

        recipeHelper = mockStatic(RecipeHelper.class);
        recipeHelper.when(() -> RecipeHelper.getUserRecipes(anyString())).thenReturn(new ArrayList<Recipe>());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("PantryPal");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.show();
    }

    @Before
    public void setUp() throws Exception {

        // clear the recipe file before each test
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @After
    public void tearDown() throws Exception {
        // clear the recipe file after each test
        Files.deleteIfExists(Paths.get(TEST_FILE));

        recipeHelper.close();
    }

    @Test
    public void sanityTest() {
        Assert.assertEquals("PantryPal 2", lookup("#titleLabel").queryAs(Label.class).getText());
    }
    /*
     * @Test
     * public void testSaveAndLoadRecipes() throws Exception {
     * // Setup
     * Recipe recipe = new Recipe("Test Recipe", "Dinner", "Ingredients", "Steps",
     * null, "testuser");
     * RecipeData.getInstance().getRecipes().clear(); // Clear existing recipes
     * RecipeData.getInstance().addRecipe(recipe); // Add a test recipe
     * 
     * RecipeData.getInstance().saveRecipes(recipe);
     * 
     * // Verify save
     * assertTrue("Recipe file should be created",
     * Files.exists(Paths.get(TEST_FILE)));
     * 
     * // Prepare for load test by clearing the recipes list
     * RecipeData.getInstance().getRecipes().clear(); // Ensure the list is clear
     * before loading
     * 
     * // Load recipes using the instance method
     * RecipeData.getInstance().loadRecipes();
     * 
     * // Verify load
     * assertEquals("There should be one recipe after loading", 1,
     * RecipeData.getInstance().getRecipes().size());
     * Recipe loadedRecipe = RecipeData.getInstance().getRecipes().get(0);
     * assertEquals("Loaded recipe name should match", "Test Recipe",
     * loadedRecipe.getName());
     * }
     * 
     * @Test
     * public void testEditRecipe() throws Exception {
     * new MainController();
     * 
     * // Add a recipe to be edited
     * RecipeData.getInstance()
     * .addRecipe(new Recipe("Original Recipe", "Lunch", "Original ingredients",
     * "Original steps", null, "testuser"));
     * // Simulate user editing a recipe
     * // For this example, let's say you're editing the name and ingredients
     * String newRecipeName = "Edited Recipe";
     * String newIngredients = "Edited ingredients";
     * String newSteps = "Edited steps";
     * 
     * // Retrieve the recipe to edit (normally you would do some sort of selection
     * or
     * // search here)
     * Recipe recipeToEdit = RecipeData.getInstance().getRecipes().get(0);
     * 
     * // Perform the edit (this logic would be in your edit handling method)
     * recipeToEdit.setName(newRecipeName);
     * recipeToEdit.setIngredients(newIngredients);
     * recipeToEdit.setSteps(newSteps);
     * 
     * // Assert that the changes have been made in the data model
     * assertEquals("Recipe name should be edited", newRecipeName,
     * recipeToEdit.getName());
     * assertEquals("Ingredients should be edited", newIngredients,
     * recipeToEdit.getIngredients());
     * assertEquals("Steps should be edited", newSteps, recipeToEdit.getSteps());
     * 
     * // Optionally, save the recipes and assert that the file has changed
     * RecipeData.getInstance().saveRecipes(recipeToEdit);
     * 
     * // Now clear the list and reload from file to ensure persistence
     * RecipeData.getInstance().getRecipes().clear();
     * 
     * // Load recipes using the instance method
     * RecipeData.getInstance().loadRecipes();
     * 
     * // Verify the first (and only) recipe is the edited one
     * Recipe reloadedRecipe = RecipeData.getInstance().getRecipes().get(0);
     * assertEquals("Reloaded recipe name should match edited name", newRecipeName,
     * reloadedRecipe.getName());
     * assertEquals("Reloaded ingredients should match edited ingredients",
     * newIngredients,
     * reloadedRecipe.getIngredients());
     * assertEquals("Reloaded steps should match edited steps", newSteps,
     * reloadedRecipe.getSteps());
     * }
     */

}