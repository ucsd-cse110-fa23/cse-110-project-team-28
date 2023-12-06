import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.After;
import org.mockito.MockedStatic;
import org.testfx.framework.junit.ApplicationTest;

import controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Recipe;
import model.UserData;
import utilites.RecipeHelper;

public class SortFilterTests extends ApplicationTest {

    final int WIDTH = 800;
    final int HEIGHT = 500;

    private MainController controller;

    MockedStatic<RecipeHelper> recipeHelper;

    @Override
    public void start(Stage primaryStage) throws Exception {
        UserData.setInstance(mock(UserData.class));
        when(UserData.getInstance().getUsername()).thenReturn("test_username");
        when(UserData.getInstance().getUserId()).thenReturn("test_id");

        recipeHelper = mockStatic(RecipeHelper.class);
        recipeHelper.when(() -> RecipeHelper.getUserRecipes(anyString())).thenReturn(new ArrayList<Recipe>());

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
}