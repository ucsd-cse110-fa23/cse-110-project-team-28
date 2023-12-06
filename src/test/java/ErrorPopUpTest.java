
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

import controller.AuthenticationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Recipe;
import model.UserData;
import utilites.RecipeHelper;
import utilites.SceneHelper;

public class ErrorPopUpTest extends ApplicationTest {

    final int WIDTH = 800;
    final int HEIGHT = 500;

    MockedStatic<RecipeHelper> recipeHelper;
    private AuthenticationController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneHelper.setPrimaryStage(primaryStage);

        UserData.setInstance(mock(UserData.class));
        when(UserData.getInstance().getUsername()).thenReturn("test_username");
        when(UserData.getInstance().getUserId()).thenReturn("test_id");

        recipeHelper = mockStatic(RecipeHelper.class);
        recipeHelper.when(() -> RecipeHelper.getUserRecipes(anyString())).thenReturn(new ArrayList<Recipe>());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/authentication.fxml"));
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
    public void testErrorPopUp() {
        clickOn("#autoLoginCheckBox");
        if(controller.autoLoginCheckBox.isSelected()) {
            try {
            clickOn("#usernameField").write("username");
            clickOn("#passwordField").write("password");
            clickOn("#loginButton");

            Assert.assertNotNull(lookup("#errorMessage").query());
            Assert.assertTrue(true);
            }
            catch (Exception e) {
                Assert.fail();
            }
        }
        else {
            Assert.fail();
        }
    }
}