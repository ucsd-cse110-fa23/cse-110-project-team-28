import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import controller.MainController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Recipe;
import model.RecipeData;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InteractiveRecipeTests extends ApplicationTest {

    final int WIDTH = 800;
    final int HEIGHT = 500;

    private String TEST_FILE = "test_recipes.json";

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        RecipeData mockRecipeData = new RecipeData();

        mockRecipeData.setFileLocation(TEST_FILE);

        RecipeData.setInstance(mockRecipeData);

        loader.getController();

        primaryStage.setTitle("PantryPal");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.show();
    }

    @Before
    public void setup() throws IOException {
        // clear the recipe list before each test
        RecipeData.getInstance().clear();

        // clear the recipe file before each test
        Files.deleteIfExists(Paths.get(TEST_FILE));

        RecipeData.getInstance().addRecipe(new Recipe("Tasty Tests", "breakfast",
                "JUnit, TestFX, Gradle, and GitHub Actions", "1. Mix well and pray your tests pass.", null));
    }

    @Test
    public void sanityTest() {
        Assert.assertEquals("PantryPal", lookup("#titleLabel").queryAs(Label.class).getText());
    }

    @Test
    public void recipePaneTest() throws IOException {
        Platform.runLater(() -> {
            Assert.assertEquals("Tasty Tests", lookup(".recipePaneLabel").queryAs(Label.class).getText());
            Assert.assertNotNull(lookup(".recipePaneDetailsButton").queryAs(Button.class));
        });
    }

    public VBox getRecipeList() {
        return lookup("#recipeList").queryAs(VBox.class);
    }

    @Test
    public void deleteRecipeTest() throws IOException {
        Platform.runLater(() -> {

            sleep(100); // without this, the test may fail because the recipe is not added to the list
                        // yet

            Assert.assertEquals(false, getRecipeList().getChildren().isEmpty());
            Assert.assertEquals("Tasty Tests", RecipeData.getInstance().getRecipes().get(0).getName());
            Assert.assertEquals("Tasty Tests", lookup(".recipePaneLabel").queryAs(Label.class).getText());

            Button recipePanelDetailsButton = lookup(".recipePaneDetailsButton").query();
            recipePanelDetailsButton.fire();

            Button deleteRecipeButton = lookup("#deleteRecipeButton").query();
            deleteRecipeButton.fire();

            // assert that recipedata is empty
            Assert.assertEquals(RecipeData.getInstance().getRecipes().isEmpty(), true);
        });
    }

    @Test
    public void editRecipeTest() {
        Platform.runLater(() -> {
            Assert.assertEquals(true, lookup("#recipeList").queryAs(VBox.class).getChildren().isEmpty());
            Assert.assertEquals("Tasty Tests", RecipeData.getInstance().getRecipes().get(0).getName());
            Assert.assertEquals("Tasty Tests", lookup(".recipePaneLabel").queryAs(Label.class).getText());

            Button recipePanelDetailsButton = lookup(".recipePaneDetailsButton").query();
            recipePanelDetailsButton.fire();

            TextArea editRecipeTextArea = lookup("#editRecipeTextArea").query();
            // TODO: this test is passing, it should not be.
            editRecipeTextArea.setText("1. Mix well and pray your tests pass. asdfasdf");

            Button saveRecipeButton = lookup("#saveRecipeButton").query();
            saveRecipeButton.fire();

            // get root node
            Parent main = lookup("#main").query();

            // get scene
            Scene scene = main.getScene();

            // print node tree
            System.out.println(scene.getRoot().toString());

            recipePanelDetailsButton = lookup(".recipePaneDetailsButton").query();
            recipePanelDetailsButton.fire();

            // confirm that the recipe was edited
            Assert.assertEquals("1. Mix well and pray your tests pass.",
                    lookup("#editRecipeTextArea").queryAs(TextArea.class).getText());
        });
    }
}
