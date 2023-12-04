import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import controller.MainController;
import javafx.scene.control.ToggleButton;
import model.Recipe;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FilterSortTests {

    @Mock
    private ToggleButton toggleBreakfast;
    @Mock
    private ToggleButton toggleLunch;
    @Mock
    private ToggleButton toggleDinner;
    @Mock
    private ComboBox<String> sortComboBox;

    private MainController controller;

    @Before
    public void setUp() {
        controller = new MainController();
        controller.toggleBreakfast = toggleBreakfast;
        controller.toggleLunch = toggleLunch;
        controller.toggleDinner = toggleDinner;
        controller.sortComboBox = sortComboBox;
    }

    @Test
    public void testApplySortAndFilter() {
        // Mocking user selection
        when(toggleBreakfast.isSelected()).thenReturn(true);
        when(sortComboBox.getValue()).thenReturn("A-Z");

        // Assuming 'getFilteredRecipes' and 'getSortedRecipes' are refactored methods
        List<Recipe> mockRecipes = new ArrayList<>();
            mockRecipes.add(new Recipe()
                .setName("Pancakes")
                .setMealType("Breakfast")
                .setIngredients("ingredients")
                .setSteps("steps")
                .setImageUrl("imageURL")
                .setId("id")
            );
            mockRecipes.add(new Recipe()
                .setName("Avocado")
                .setMealType("Lunch")
                .setIngredients("ingredients")
                .setSteps("steps")
                .setImageUrl("imageURL")
                .setId("id")
            );
            mockRecipes.add(new Recipe()
                .setName("Omelette")
                .setMealType("Breakfast")
                .setIngredients("ingredients")
                .setSteps("steps")
                .setImageUrl("imageURL")
                .setId("id")
            );

        
        controller.recipes = mockRecipes;

        List<Recipe> filteredAndSorted = controller.applySortAndFilter();

        assertEquals(2, filteredAndSorted.size()); // Only breakfast items
        assertEquals("Omelette", filteredAndSorted.get(0).getName()); // Sorted A-Z
        assertEquals("Pancakes", filteredAndSorted.get(1).getName());
    }
}
