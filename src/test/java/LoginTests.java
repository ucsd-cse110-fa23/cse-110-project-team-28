
import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LoginTests extends ApplicationTest {

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
    public void sanityTest() {
        Assert.assertNotNull(lookup("#usernameField").query());
    }

    @Test
    public void testEmptyLogin() {
        clickOn("#passwordField");
        clickOn("#loginButton");
        FxAssert.verifyThat("#errorLabel", LabeledMatchers.hasText("Please enter a username and password."));
    }

    @Test
    public void testEmptyUsername() {
        clickOn("#passwordField").write("password");
        clickOn("#loginButton");
        Assert.assertEquals("Please enter a username and password.",
                lookup("#errorLabel").queryAs(Label.class).getText());
    }

    @Test
    public void testEmptyPassword() {
        clickOn("#usernameField").write("username");
        clickOn("#loginButton");
        Assert.assertEquals("Please enter a username and password.",
                lookup("#errorLabel").queryAs(Label.class).getText());
    }
}