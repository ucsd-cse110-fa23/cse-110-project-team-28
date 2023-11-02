import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
        this.setStyle("-fx-background-color: #F0F8FF;");
    }
    
    

    public void saveRecipes() {
        //TODO
    }
}

class Footer extends HBox {
    private Button addButton;
    private Button deleteButton;
    private Button saveButton;

    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        addButton = new Button("Add Recipe");
        addButton.setStyle(defaultButtonStyle);

        /* idk about this, i think we just delete when delete is clicked, unlike lab 1
        deleteButton = new Button("Delete");
        deleteButton.setStyle(defaultButtonStyle);*/

        saveButton = new Button("Save Recipes");
        saveButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(addButton, saveButton); //delete button maybe
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
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("PantryPal");
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
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

    public void addListeners() {
        addButton.setOnAction(e -> {
            Recipe recipe = new Recipe();
            recipeList.getChildren().add(0, recipe);

            Button deleteButton = recipe.getDeleteButton();
            deleteButton.setOnAction(e1 -> {
                recipeList.getChildren().remove(recipe);
            });
        });
    }
}

public class RecipeApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppFrame root = new AppFrame();

        primaryStage.setTitle("PantryPal");
        primaryStage.setScene(new Scene(root, 500, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
