
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SignUpTests extends ApplicationTest {

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
    public void testEmptySignUp() {
        clickOn("#signUpButton");
        Assert.assertEquals("Please enter a username and password.",
                lookup("#errorLabel").queryAs(Label.class).getText());
    }

    @Test
    public void testEmptyUsername() {
        clickOn("#passwordField").write("password");
        clickOn("#signUpButton");
        Assert.assertEquals("Please enter a username and password.",
                lookup("#errorLabel").queryAs(Label.class).getText());
    }

    @Test
    public void testEmptyPassword() {
        clickOn("#usernameField").write("username");
        clickOn("#signUpButton");
        Assert.assertEquals("Please enter a username and password.",
                lookup("#errorLabel").queryAs(Label.class).getText());
    }
}