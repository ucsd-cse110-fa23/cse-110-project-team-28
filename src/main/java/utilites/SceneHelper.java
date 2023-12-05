package utilites;

import java.io.IOException;
import java.net.URL;

import controller.EditRecipeController;
import controller.RecipeDetailsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Recipe;

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

    public static void switchToEditRecipeScene(Recipe recipe) throws IOException {
        URL sceneUrl = SceneHelper.class.getResource("/fxml/editRecipe.fxml");
        FXMLLoader loader = new FXMLLoader(sceneUrl);
        Parent root = loader.load();
        Scene newScene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());

        primaryStage.setScene(newScene);
        primaryStage.show();

        EditRecipeController controller = loader.getController();
        controller.setRecipe(recipe);
    }

    public static void switchToNewRecipeScene() throws IOException {
        URL sceneUrl = SceneHelper.class.getResource("/fxml/newRecipe.fxml");
        Parent root = FXMLLoader.load(sceneUrl);
        Scene newScene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());

        primaryStage.setScene(newScene);
        primaryStage.show();
    }

    public static void switchToNewRecipeDebugScene() throws IOException {
        URL sceneUrl = SceneHelper.class.getResource("/fxml/newRecipeDebug.fxml");
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

    public static void switchToRecipeDetailsScene(Recipe recipe) throws IOException {
        URL sceneUrl = SceneHelper.class.getResource("/fxml/recipeDetails.fxml");
        FXMLLoader loader = new FXMLLoader(sceneUrl);
        Parent root = loader.load();
        Scene newScene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());

        primaryStage.setScene(newScene);
        primaryStage.show();

        RecipeDetailsController controller = loader.getController();
        controller.setRecipe(recipe);
    }
}
