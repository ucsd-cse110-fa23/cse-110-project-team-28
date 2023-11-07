import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
        Recipe recipe = new Recipe();
        recipe.setMealType("Dinner");
        recipe.setIngredients("Chicken");
        recipe.setSteps("1. Cook \n2. Serve");

        assertEquals("Dinner", recipe.getMealType());
        assertEquals("Chicken", recipe.getIngredients());
        assertEquals("1. Cook \n2. Serve", recipe.getSteps());
    }
}
