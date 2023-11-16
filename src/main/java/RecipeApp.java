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
        RecipeData.getInstance().loadRecipes();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("PantryPal");

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(true);

        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);


        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
