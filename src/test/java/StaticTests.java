import static org.junit.Assert.assertEquals;
import org.junit.Test;

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

}
