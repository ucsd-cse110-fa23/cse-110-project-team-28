import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class RecipeAppTest {

    private boolean platformStarted = false;

    @BeforeClass
    public static void onlyOnce() {
        // run toolkit
        javafx.application.Platform.startup(() -> {
        });
    }

    @Before
    public void setUp() {
        // ...
    }

    @Test
    public void testBananas() {
        RecipeInputWindow inputWindow = mock(RecipeInputWindow.class);
        when(inputWindow.getIngredients()).thenReturn("bananas");

        assertEquals("bananas", inputWindow.getIngredients());
    }

    @Test
    public void testRecipeCreation() {
        Recipe recipe = new Recipe(null);
        recipe.setMealType("Dinner");
        recipe.setIngredients("Chicken");
        recipe.setSteps("1. Cook \n2. Serve");

        assertEquals("Dinner", recipe.getMealType());
        assertEquals("Chicken", recipe.getIngredients());
        assertEquals("1. Cook \n2. Serve", recipe.getSteps());
    }

    @Test
    public void testBackButtonClosesWindow() {
        Platform.runLater(() -> {
            Recipe recipe = new Recipe(null);
            RecipeDetailWindow window = new RecipeDetailWindow(recipe, null);

            // Open window for testing
            window.show();

            // Simulate back button press
            window.getBackButton().fire();

            // Assert window is not showing after back button press
            assertFalse(window.isShowing());
        });
    }

    @Test
    public void testCancelButtonClosesWindow() {
        Platform.runLater(() -> {
            RecipeList recipeList = new RecipeList();
            AppFrame appFrame = new AppFrame();
            RecipeInputWindow window = new RecipeInputWindow(recipeList, appFrame);

            // Open window for testing
            window.show();

            // Simulate cancel button press
            window.getCancelButton().fire();

            // Assert window is not showing after cancel button press
            assertFalse(window.isShowing());
        });
    }

    @Test
    public void testDeleteButtonRemovesRecipe() {
        Platform.runLater(() -> {
            Recipe recipe = new Recipe(null);
            RecipeDetailWindow window = new RecipeDetailWindow(recipe, null);

            // Open window for testing
            window.show();

            // Simulate back button press
            window.getDeleteButton().fire();

            // Assert window is not showing after back button press
            assertFalse(window.isShowing());
        });
    }
    
}