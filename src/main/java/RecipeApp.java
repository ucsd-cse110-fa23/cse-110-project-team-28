import controller.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.RecipeData;
import javafx.fxml.FXMLLoader;

public class RecipeApp extends Application {

    final int WIDTH = 800;
    final int HEIGHT = 500;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/authentication.fxml"));
        primaryStage.setTitle("Sign Up");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
