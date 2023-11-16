package utilites;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneHelper {
    public static void switchToMainScene(Scene scene) throws IOException {
        Stage stage = (Stage) scene.getWindow();
        URL sceneUrl = SceneHelper.class.getResource("/fxml/main.fxml");
        Parent root = FXMLLoader.load(sceneUrl);
        Scene newScene = new Scene(root, scene.getWidth(), scene.getHeight());

        stage.setScene(newScene);
        stage.show();
    }

    public static void switchToEditRecipeScene(Scene scene) throws IOException {
        Stage stage = (Stage) scene.getWindow();
        URL sceneUrl = SceneHelper.class.getResource("/fxml/editRecipe.fxml");
        Parent root = FXMLLoader.load(sceneUrl);
        Scene newScene = new Scene(root, scene.getWidth(), scene.getHeight());

        stage.setScene(newScene);
        stage.show();
    }

    public static void switchToNewRecipeScene(Scene scene) throws IOException {
        Stage stage = (Stage) scene.getWindow();
        URL sceneUrl = SceneHelper.class.getResource("/fxml/newRecipe.fxml");
        Parent root = FXMLLoader.load(sceneUrl);
        Scene newScene = new Scene(root, scene.getWidth(), scene.getHeight());

        stage.setScene(newScene);
        stage.show();
    }
}
