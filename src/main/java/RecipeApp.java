import controller.AuthenticationController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import utilites.InitializeHelper;
import javafx.fxml.FXMLLoader;
import java.util.prefs.Preferences;

import config.Config;
import utilites.SceneHelper;

public class RecipeApp extends Application {

    final static int WIDTH = 800;
    final static int HEIGHT = 500;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // do not initialize mongodb for client
        Config.init();
        SceneHelper.setPrimaryStage(primaryStage);

        Font.loadFont(getClass().getResourceAsStream("fonts/PPEiko-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("fonts/PPEiko-Medium.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("fonts/PPNeueMachina-Regular.ttf"), 12);

        primaryStage.setTitle("PantryPal 2");
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setResizable(true);

        Preferences prefs = Preferences.userNodeForPackage(AuthenticationController.class);
        String username = prefs.get("username", null);
        String password = prefs.get("password", null);

        if (username != null && password != null) {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
            primaryStage.setTitle("PantryPal 2");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

            primaryStage.setScene(scene);
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/authentication.fxml"));
            primaryStage.setTitle("PantryPal 2");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

            primaryStage.setScene(scene);
        }

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}