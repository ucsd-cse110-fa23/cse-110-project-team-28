import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.testfx.framework.junit.ApplicationTest;

import controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Recipe;
import model.UserData;
import utilites.RecipeHelper;

public class SortFilterTests extends ApplicationTest {

    final int WIDTH = 800;
    final int HEIGHT = 500;

    private MainController controller;

    MockedStatic<RecipeHelper> recipeHelper;

    ArrayList<Recipe> recipes;

    @Override
    public void start(Stage primaryStage) throws Exception {
        UserData.setInstance(mock(UserData.class));
        when(UserData.getInstance().getUsername()).thenReturn("test_username");
        when(UserData.getInstance().getUserId()).thenReturn("test_id");

        // initialize recipes
        recipes = new ArrayList<Recipe>();
        recipes.add(new Recipe()
                .setName("test_recipe_1")
                .setMealType("breakfast"));
        recipes.add(new Recipe()
                .setName("test_recipe_2")
                .setMealType("lunch"));
        recipes.add(new Recipe()
                .setName("test_recipe_3")
                .setMealType("dinner"));
        recipes.add(new Recipe()
                .setName("test_recipe_4")
                .setMealType("breakfast"));
        recipes.add(new Recipe()
                .setName("test_recipe_5")
                .setMealType("lunch"));
        recipes.add(new Recipe()
                .setName("test_recipe_6")
                .setMealType("breakfast"));

        recipeHelper = mockStatic(RecipeHelper.class);
        recipeHelper.when(() -> RecipeHelper.getUserRecipes(anyString())).thenReturn(recipes);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();
        this.controller = loader.getController();

        primaryStage.setTitle("PantryPal");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.show();
    }

    @After
    public void tearDown() throws Exception {
        recipeHelper.close();
    }

    @Test
    public void sanityTest() {
        assertEquals(6, lookup("#recipeList").queryAs(VBox.class).getChildren().size());
    }

    @Test
    public void filterBreakfastTest() {
        clickOn("#breakfastToggleButton");
        assertEquals(3, lookup("#recipeList").queryAs(VBox.class).getChildren().size());
    }

    @Test
    public void filterLunchTest() {
        clickOn("#lunchToggleButton");
        assertEquals(2, lookup("#recipeList").queryAs(VBox.class).getChildren().size());
    }

    @Test
    public void filterDinnerTest() {
        clickOn("#dinnerToggleButton");
        assertEquals(1, lookup("#recipeList").queryAs(VBox.class).getChildren().size());
    }

    @Test
    public void filterBreakfastLunchTest() {
        clickOn("#breakfastToggleButton");
        clickOn("#lunchToggleButton");
        assertEquals(5, lookup("#recipeList").queryAs(VBox.class).getChildren().size());
    }

    @Test
    public void filterBreakfastDinnerTest() {
        clickOn("#breakfastToggleButton");
        clickOn("#dinnerToggleButton");
        assertEquals(4, lookup("#recipeList").queryAs(VBox.class).getChildren().size());
    }

    @Test
    public void filterLunchDinnerTest() {
        clickOn("#lunchToggleButton");
        clickOn("#dinnerToggleButton");
        assertEquals(3, lookup("#recipeList").queryAs(VBox.class).getChildren().size());
    }

    @Test
    public void filterAllTest() {
        clickOn("#breakfastToggleButton");
        clickOn("#lunchToggleButton");
        clickOn("#dinnerToggleButton");
        assertEquals(6, lookup("#recipeList").queryAs(VBox.class).getChildren().size());
    }

    @Test
    public void sortOldestToNewestTest() {
        ComboBox sortComboBox = lookup("#sortComboBox").queryAs(ComboBox.class);

        interact(() -> sortComboBox.getSelectionModel().select(1));

        assertEquals("test_recipe_1",
                ((Label) (lookup("#recipeList").queryAs(VBox.class).lookup(".recipePaneLabel"))).getText());
    }

    @Test
    public void sortNewestToOldestTest() {
        ComboBox sortComboBox = lookup("#sortComboBox").queryAs(ComboBox.class);

        interact(() -> sortComboBox.getSelectionModel().select(0));

        assertEquals("test_recipe_6",
                ((Label) (lookup("#recipeList").queryAs(VBox.class).lookup(".recipePaneLabel"))).getText());
    }

    @Test
    public void sortAtoZTest() {
        ComboBox sortComboBox = lookup("#sortComboBox").queryAs(ComboBox.class);

        interact(() -> sortComboBox.getSelectionModel().select(2));

        assertEquals("test_recipe_1",
                ((Label) (lookup("#recipeList").queryAs(VBox.class).lookup(".recipePaneLabel"))).getText());
    }

    @Test
    public void sortZtoATest() {
        ComboBox sortComboBox = lookup("#sortComboBox").queryAs(ComboBox.class);

        interact(() -> sortComboBox.getSelectionModel().select(3));

        assertEquals("test_recipe_6",
                ((Label) (lookup("#recipeList").queryAs(VBox.class).lookup(".recipePaneLabel"))).getText());
    }
}