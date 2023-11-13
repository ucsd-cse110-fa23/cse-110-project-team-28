import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import controller.MainController;
import model.Recipe;

public class StaticTests {

    @Test
    public void testRecipeCreation() {
        Recipe recipe = new Recipe();
        recipe.setName("Roasted Chicken");
        recipe.setMealType("Dinner");
        recipe.setIngredients("Chicken");
        recipe.setSteps("1. Roast Chicken \n2. Serve");

        assertEquals("Roasted Chicken", recipe.getName());
        assertEquals("Dinner", recipe.getMealType());
        assertEquals("Chicken", recipe.getIngredients());
        assertEquals("1. Roast Chicken \n2. Serve", recipe.getSteps());
    }

    @Test
    public void testRecipeToJson() {
        // Setup
        Recipe recipe = new Recipe("Test Recipe", "Dinner", "Ingredients", "Steps");

        // Execute
        String json = recipe.toJson();

        // Verify
        assertNotNull("The serialized JSON should not be null", json);
        assertFalse("The serialized JSON should not be empty", json.isEmpty());
    }

    @Test
    public void testJsonToRecipe() {
        // Setup
        String json = "{\"name\":\"Test Recipe\",\"mealType\":\"Dinner\",\"ingredients\":\"Ingredients\",\"steps\":\"Steps\"}";

        // Execute
        Recipe recipe = Recipe.fromJson(json);

        // Verify
        assertNotNull("The deserialized recipe should not be null", recipe);
        assertEquals("The name of the recipe should match", "Test Recipe", recipe.getName());
        assertEquals("The meal type of the recipe should match", "Dinner", recipe.getMealType());
        assertEquals("The ingredients of the recipe should match", "Ingredients", recipe.getIngredients());
        assertEquals("The steps of the recipe should match", "Steps", recipe.getSteps());
    }
        
}