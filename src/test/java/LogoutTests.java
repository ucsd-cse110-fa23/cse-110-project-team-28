
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.service.query.EmptyNodeQueryException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Recipe;
import model.UserData;
import utilites.RecipeHelper;
import utilites.SceneHelper;

public class LogoutTests extends ApplicationTest {

    final int WIDTH = 800;
    final int HEIGHT = 500;

    MockedStatic<RecipeHelper> recipeHelper;

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneHelper.setPrimaryStage(primaryStage);

        UserData.setInstance(mock(UserData.class));
        when(UserData.getInstance().getUsername()).thenReturn("test_username");
        when(UserData.getInstance().getUserId()).thenReturn("test_id");

        recipeHelper = mockStatic(RecipeHelper.class);
        recipeHelper.when(() -> RecipeHelper.getUserRecipes(anyString())).thenReturn(new ArrayList<Recipe>());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

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
        Assert.assertNotNull(lookup("#titleLabel").query());
    }

    @Test
    public void testLogout() {
        clickOn("#logoutButton");
        try {
            lookup("#titleLabel").query();
            Assert.fail();
        } catch (EmptyNodeQueryException e) {
            Assert.assertTrue(true);
        }
    }
}