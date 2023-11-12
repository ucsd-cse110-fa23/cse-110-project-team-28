import java.io.IOException;
import org.junit.Assert;
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
}
