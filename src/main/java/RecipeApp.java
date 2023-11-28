import controller.AuthenticationController;
import controller.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.RecipeData;
import javafx.fxml.FXMLLoader;
import java.util.prefs.Preferences;

public class RecipeApp extends Application {

    final int WIDTH = 800;
    final int HEIGHT = 500;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Preferences prefs = Preferences.userNodeForPackage(AuthenticationController.class);
        String username = prefs.get("username", null);
        String password = prefs.get("password", null);

        if (username != null && password != null) {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
            primaryStage.setTitle("PantryPal 2");
            primaryStage.setScene(new Scene(root));
            primaryStage.setWidth(WIDTH);
            primaryStage.setHeight(HEIGHT);
            primaryStage.show();
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/authentication.fxml"));
            primaryStage.setTitle("Sign Up");
            primaryStage.setScene(new Scene(root));
            primaryStage.setWidth(WIDTH);
            primaryStage.setHeight(HEIGHT);
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
