
//import org.assertj.core.internal.Paths;
//import static org.assertj.core.api.Assertions.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextMatchers;
import controller.MainController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Recipe;
import model.RecipeData;

public class InteractiveTests extends ApplicationTest {

    final int WIDTH = 800;
    final int HEIGHT = 500;

    private MainController controller;

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
        FxAssert.verifyThat("#title", TextMatchers.hasText("PantryPal"));
    }

    @Test
    public void testListCreation() throws IOException {
        Platform.runLater(() -> {
            controller.addRecipe(new Recipe("Tasty Tests", "breakfast", "JUnit, TestFX, Gradle, and GitHub Actions",
                    "1. Mix well and pray your tests pass."));

            FxAssert.verifyThat(".recipePaneTitle", LabeledMatchers.hasText("Tasty Tests"));

        });
    }

    @Test
    public void deleteRecipe() throws IOException {
        Platform.runLater(() -> {
            Assert.assertEquals(controller.getRecipeList().getChildren().isEmpty(), true);

            controller.addRecipe(new Recipe("Tasty Tests", "breakfast", "JUnit, TestFX, Gradle, and GitHub Actions",
                    "1. Mix well and pray your tests pass."));

            sleep(100);

            Assert.assertEquals(false, controller.getRecipeList().getChildren().isEmpty());
            Assert.assertEquals("Tasty Tests",
                    ((Label) ((GridPane) controller.getRecipeList().getChildren().get(0)).getChildren().get(0))
                            .getText());

            Button recipePanelDetailsButton = lookup(".recipePaneDetailsButton").query();
            recipePanelDetailsButton.fire();

            Button deleteRecipeButton = lookup("#deleteRecipeButton").query();
            deleteRecipeButton.fire();

            // assert that recipedata is empty
            Assert.assertEquals(RecipeData.getInstance().getRecipes().isEmpty(), true);
        });
    }
/* 
    @Before
    public void setUp() throws Exception {
        RecipeData.getInstance().getRecipes().clear(); // Clear any existing recipes
        controller = new MainController();
        controller.setRecipeFile(TEST_RECIPE_FILE); // Set the test file path
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(Paths.get(TEST_RECIPE_FILE)); // Delete the test file
        RecipeData.getInstance().getRecipes().clear(); // Clear recipes after test
    }*/
 
    @Test
    public void testSaveAndLoadRecipes() throws Exception {
       // Setup
       Recipe recipe = new Recipe("Test Recipe", "Dinner", "Ingredients", "Steps");
       RecipeData.getInstance().getRecipes().clear(); // Clear existing recipes
       RecipeData.getInstance().addRecipe(recipe); // Add a test recipe

       MainController controller = new MainController();
       controller.setRecipeFile(TEST_RECIPE_FILE); 

       // Execute save
       controller.saveAllRecipesToFile(); // Save current recipes to file

       // Verify save
       assertTrue("Recipe file should be created", Files.exists(Paths.get(TEST_RECIPE_FILE)));

       // Prepare for load test by clearing the recipes list
       RecipeData.getInstance().getRecipes().clear(); // Ensure the list is clear before loading

       // Execute load
       controller.loadRecipes();

       // Verify load
       RecipeData recipeData = RecipeData.getInstance();
       assertEquals("There should be one recipe after loading", 1, recipeData.getRecipes().size());
       Recipe loadedRecipe = recipeData.getRecipes().get(0);
       assertEquals("Loaded recipe name should match", "Test Recipe", loadedRecipe.getName());
    }

}
