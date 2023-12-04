package utilites;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneHelper {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        SceneHelper.primaryStage = primaryStage;
    }

    public static void switchToMainScene() throws IOException {
        URL sceneUrl = SceneHelper.class.getResource("/fxml/main.fxml");
        Parent root = FXMLLoader.load(sceneUrl);
        Scene newScene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());

        primaryStage.setScene(newScene);
        primaryStage.show();
    }

    public static void switchToEditRecipeScene() throws IOException {
        URL sceneUrl = SceneHelper.class.getResource("/fxml/editRecipe.fxml");
        Parent root = FXMLLoader.load(sceneUrl);
        Scene newScene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());

        primaryStage.setScene(newScene);
        primaryStage.show();
    }

    public static void switchToNewRecipeScene() throws IOException {
        URL sceneUrl = SceneHelper.class.getResource("/fxml/newRecipe.fxml");
        Parent root = FXMLLoader.load(sceneUrl);
        Scene newScene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());

        primaryStage.setScene(newScene);
        primaryStage.show();
    }

    public static void switchToAuthenticationScene() throws IOException {
        URL sceneUrl = SceneHelper.class.getResource("/fxml/authentication.fxml");
        Parent root = FXMLLoader.load(sceneUrl);
        Scene newScene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());

        primaryStage.setScene(newScene);
        primaryStage.show();
    }
}
