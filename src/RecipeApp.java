import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import multithreading.RecordingAppFrame;


class Recipe extends HBox {
    private TextField recipeName;
    private Button deleteButton;

    Recipe() {
        this.setPrefSize(500, 20);
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");

        recipeName = new TextField();
        recipeName.setPrefSize(380, 20);
        recipeName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        recipeName.setPadding(new Insets(10, 0, 10, 0));
        this.getChildren().add(recipeName);

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
}

class RecipeList extends VBox {
    RecipeList() {
        this.setSpacing(5);
        this.setPrefSize(500, 560);
        this.getStyleClass().add("recipe-list");
    }

    public void saveRecipes() {
        // TODO
    }
}

class Footer extends HBox {
    private Button addButton;
    private Button deleteButton;
    private Button saveButton;

    Footer() {
        this.setPrefSize(500, 60);
        this.setSpacing(15);
        this.getStyleClass().add("footer");

        Font customFont = Font.loadFont("file:src/resources/fonts/Roboto-Medium.ttf", 12);

        addButton = new Button();
        addButton.getStyleClass().add("add-button");
        addButton.setText("Add Recipe");
        addButton.setFont(customFont);

        Image addImage = new Image(getClass().getResourceAsStream("resources/images/add.png"));
        addButton.setGraphic(new ImageView(addImage));

        saveButton = new Button();
        saveButton.getStyleClass().add("save-button");
        saveButton.setText("Save Recipes");
        saveButton.setFont(customFont);

        // using download icon for save button
        Image saveImage = new Image(getClass().getResourceAsStream("resources/images/download.png"));
        saveButton.setGraphic(new ImageView(saveImage));

        this.getChildren().addAll(addButton, saveButton);
        this.setAlignment(Pos.CENTER);
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }
}

class Header extends HBox {
    Header() {
        this.setPrefSize(500, 60);
        this.getStyleClass().add("header");

        Font customFont = Font.loadFont("file:src/resources/fonts/Roboto-Black.ttf", 24);

        Text titleText = new Text("PantryPal");
        titleText.setId("title-text");
        titleText.setFont(customFont);

        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER);
    }
}

class AppFrame extends BorderPane {
    private Header header;
    private Footer footer;
    private RecipeList recipeList;

    private Button addButton;
    private Button saveButton;

    AppFrame() {
        header = new Header();
        recipeList = new RecipeList();
        footer = new Footer();

        ScrollPane scrollPane = new ScrollPane(recipeList);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("scroll-pane");

        this.setTop(header);
        this.setCenter(scrollPane);
        this.setBottom(footer);

        addButton = footer.getAddButton();
        saveButton = footer.getSaveButton();
        saveButton.setOnAction(e -> {
            recipeList.saveRecipes();
        });

        addListeners();
    }

    public void addDeleteListener(Recipe recipe) {
        Button deleteButton = recipe.getDeleteButton();
        deleteButton.setOnAction(e -> {
            recipeList.getChildren().remove(recipe);
        });
    }

    public void addListeners() {

        addButton.setOnAction(e -> {
            RecipeInputWindow recipeInputWindow = new RecipeInputWindow(recipeList,
                    this);
            recipeInputWindow.show();

            for (Node node : recipeList.getChildren()) {
                if (node instanceof Recipe) {
                    addDeleteListener((Recipe) node);
                }
            }
        });

    }

}
 
/*
 * Class for the seperate window for user to enter recipe parameters (meal type, ingredients) for recipe creation 
 */
class RecipeInputWindow extends Stage {

    private TextField recipeNameField;
    private Button saveButton;
    private RecipeList recipeList;
    private AppFrame appFrame;
    private Button recordButton;

    RecipeInputWindow(RecipeList recipeList, AppFrame appFrame) {
        this.recipeList = recipeList;
        this.appFrame = appFrame;

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        recipeNameField = new TextField();
        recipeNameField.setPromptText("Enter Recipe Name");

        saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveRecipe());

        RecordingAppFrame recorder = new RecordingAppFrame();

        layout.getChildren().addAll(recipeNameField, saveButton, recorder);

        Scene scene = new Scene(layout, 300, 200);
        this.setScene(scene);
        this.setTitle("Add New Recipe");
    }

    /*
     * Saves recipe to main app list
     */
    private void saveRecipe() {
        String recipeName = recipeNameField.getText();
        Recipe recipe = new Recipe();
        if (!recipeName.isEmpty()) {

            recipe.getRecipeName().setText(recipeName);
            recipeList.getChildren().add(0, recipe);
            this.close();
        }
        appFrame.addDeleteListener(recipe);
    }

    private void recordRecipe(){

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

        Scene scene = new Scene(layout, 300, 200);
        this.setScene(scene);
        this.setTitle("Recipe: "+ recipe.getRecipeName().getText());
    }
}

public class RecipeApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppFrame root = new AppFrame();

        primaryStage.setTitle("PantryPal");

        Scene scene = new Scene(root, 500, 500);
        scene.getStylesheets().add("resources/css/style.css");

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
