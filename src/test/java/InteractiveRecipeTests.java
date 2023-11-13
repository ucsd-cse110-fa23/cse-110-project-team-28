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

public class InteractiveRecipeTests extends ApplicationTest {

    final int WIDTH = 800;
    final int HEIGHT = 500;

    private MainController controller;

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

    @Before
    public void setup() {
        RecipeData.getInstance().getRecipes().clear();
        RecipeData.getInstance().removeObserver();

        RecipeData.getInstance().addRecipe(new Recipe("Tasty Tests", "breakfast",
                "JUnit, TestFX, Gradle, and GitHub Actions", "1. Mix well and pray your tests pass."));
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

    @Test
    public void deleteRecipeTest() throws IOException {
        Platform.runLater(() -> {
            controller.setRecipes(RecipeData.getInstance().getRecipes());

            sleep(100); // without this, the test may fail because the recipe is not added to the list
                        // yet

            Assert.assertEquals(false, controller.getRecipeList().getChildren().isEmpty());
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
            Assert.assertEquals(false, lookup("#recipeList").queryAs(VBox.class).getChildren().isEmpty());
            Assert.assertEquals("Tasty Tests", RecipeData.getInstance().getRecipes().get(0).getName());
            Assert.assertEquals("Tasty Tests", lookup(".recipePaneLabel").queryAs(Label.class).getText());

            Button recipePanelDetailsButton = lookup(".recipePaneDetailsButton").query();
            recipePanelDetailsButton.fire();

            TextArea editRecipeTextArea = lookup("#editRecipeTextArea").query();
            editRecipeTextArea.setText("1. Mix well and pray your tests pass.\n2. If they don't, cry.");

            Button saveRecipeButton = lookup("#saveRecipeButton").query();
            saveRecipeButton.fire();

            // confirm navigation back to main screen
            Assert.assertEquals("PantryPal", lookup("#titleLabel").queryAs(Label.class).getText());

            Assert.assertEquals(false, controller.getRecipeList().getChildren().isEmpty());
            Assert.assertEquals("Tasty Tests", RecipeData.getInstance().getRecipes().get(0).getName());
            Assert.assertEquals("Tasty Tests", lookup(".recipePaneLabel").queryAs(Label.class).getText());

            recipePanelDetailsButton = lookup(".recipePaneDetailsButton").query();
            recipePanelDetailsButton.fire();

            // confirm that the recipe was edited
            Assert.assertEquals("1. Mix well and pray your tests pass.\n2. If they don't, cry.",
                    lookup("#editRecipeTextArea").queryAs(TextArea.class).getText());
        });
    }
}
