package utilites;

import controller.ErrorController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ErrorPopupHelper {
    public static void showErrorPopup(String error) {
        try {
            FXMLLoader loader = new FXMLLoader(ErrorPopupHelper.class.getResource("/fxml/errorPopup.fxml"));
            Parent root = loader.load();

            ErrorController controller = loader.getController();
            controller.setErrorMessage(error);

            Stage stage = new Stage();

            stage.setTitle("Error");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
