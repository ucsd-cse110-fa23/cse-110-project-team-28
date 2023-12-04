import org.junit.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.service.query.EmptyNodeQueryException;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Recipe;
import model.UserData;
import utilites.AuthHelper;
import utilites.RecipeHelper;
import utilites.SceneHelper;

public class TestErrorPopup extends ApplicationTest {
    final int WIDTH = 800;
    final int HEIGHT = 500;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/authentication.fxml"));
        primaryStage.setTitle("Sign Up");

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    @Test
    public void testLoginErrorPopupWhenServerDown() {
        // Mock the server response to simulate server down
        mockServerResponseForLogin();

        // Launch the login view
        clickOn("#usernameField").write("testuser");
        clickOn("#passwordField").write("password");
        clickOn("#loginButton");

        // Assert that the error popup is displayed
        verifyThat("#errorPopup", isVisible());
        verifyThat("#errorMessage", LabeledMatchers.hasText("Error connecting to the server. Please try again later."));
    }

    private void mockServerResponseForLogin() {
        try (MockedStatic<AuthHelper> mockedAuthHelper = Mockito.mockStatic(AuthHelper.class)) {
            mockedAuthHelper.when(() -> AuthHelper.login(anyString(), anyString()))
                    .thenThrow(new IOException("Server is down"));
        }
    }

}
