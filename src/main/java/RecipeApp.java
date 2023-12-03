import controller.AuthenticationController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilites.InitializeHelper;
import javafx.fxml.FXMLLoader;
import java.util.prefs.Preferences;

public class RecipeApp extends Application {

    final static int WIDTH = 800;
    final static int HEIGHT = 500;

    @Override
    public void start(Stage primaryStage) throws Exception {
        InitializeHelper.init();

        Preferences prefs = Preferences.userNodeForPackage(AuthenticationController.class);
        String username = prefs.get("username", null);
        String password = prefs.get("password", null);

        if (username != null && password != null) {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
            primaryStage.setTitle("PantryPal 2");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setWidth(WIDTH);
            primaryStage.setHeight(HEIGHT);
            primaryStage.setResizable(true);
            primaryStage.show();
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/authentication.fxml"));
            primaryStage.setTitle("Sign Up");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setWidth(WIDTH);
            primaryStage.setHeight(HEIGHT);
            primaryStage.setResizable(true);
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}