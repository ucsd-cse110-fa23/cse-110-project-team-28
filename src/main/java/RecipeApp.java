import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import javafx.fxml.FXMLLoader;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONException;

import controller.MainController;
import controller.RecipePaneController;
import multithreading.*;
import utilites.ChatGPT;

class Recipe extends HBox {
    // Recipe atcual attributes
    private TextField recipeName;
    private String ingredients; // ingredients that user provides
    private String steps; // the gpt generated recipe (maybe needs a better name)

    // Recipe UI attributes in the main app view
    private Label mealTypeLabel;
    private String mealType;
    private Button deleteButton;

    Recipe() {
        this.setPrefSize(500, 20);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");

        recipeName = new TextField();
        recipeName.setPrefSize(300, 20);
        recipeName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        recipeName.setPadding(new Insets(10, 0, 10, 0));
        recipeName.setEditable(false);
        this.getChildren().add(recipeName);

        // meal type label
        mealTypeLabel = new Label(mealType);
        mealTypeLabel.setPrefSize(100, 20);
        mealTypeLabel.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        mealTypeLabel.setPadding(new Insets(10, 0, 10, 0));
        this.getChildren().add(mealTypeLabel);

        deleteButton = new Button("Delete");
        deleteButton.setPrefSize(100, 20);
        deleteButton.setPrefHeight(Double.MAX_VALUE);
        deleteButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        this.getChildren().add(deleteButton);

        recipeName.setOnMouseClicked(e -> openDetailWindow());
    }

    private void openDetailWindow() {
        RecipeDetailWindow recipeDetailWindow = new RecipeDetailWindow(this);
        recipeDetailWindow.show();
    }

    public TextField getRecipeName() {
        return this.recipeName;
    }

    public Button getDeleteButton() {
        return this.deleteButton;
    }

    // meal type stuff next two methods
    public void setMealType(String mealType) {
        this.mealType = mealType;
        mealTypeLabel.setText(mealType);
    }

    public String getMealType() {
        return this.mealType;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getSteps() {
        return this.steps;
    }
}

/*
 * Window for for actual recipe (ingredients + instructions)
 */
class RecipeDetailWindow extends Stage {

    private Recipe recipe;

    RecipeDetailWindow(Recipe recipe) {
        this.recipe = recipe;

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Label recipeNameLabel = new Label("Recipe: " + recipe.getRecipeName().getText());
        recipeNameLabel.setStyle("-fx-font-weight: bold;");

        // Other UI components for displaying recipe details
        TextArea recipeStepsArea = new TextArea();
        // recipeStepsArea.setEditable(false);
        recipeStepsArea.setText(recipe.getSteps());
        recipeStepsArea.setPrefWidth(300);
        recipeStepsArea.setPrefHeight(480);

        Scene scene = new Scene(layout, 500, 600);
        this.setScene(scene);
        this.setTitle("Recipe: " + recipe.getRecipeName().getText());

        layout.getChildren().addAll(recipeNameLabel, recipeStepsArea);
    }
}

public class RecipeApp extends Application {

    final int WIDTH = 800;
    final int HEIGHT = 500;
    final int WINDOW_PADDING = 10;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("PantryPal");

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

        primaryStage.setScene(scene);
        // resizing is not super stable right now
        primaryStage.setResizable(false);

        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
