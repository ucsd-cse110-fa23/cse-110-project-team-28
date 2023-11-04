import javafx.application.Application;
import javafx.scene.Node;
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

import multithreading.RecordingAppFrame;


class Recipe extends HBox {
    private TextField recipeName;
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
}

class RecipeList extends VBox {
    RecipeList() {
        this.setSpacing(5);
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
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
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        addButton = new Button("Add Recipe");
        addButton.setStyle(defaultButtonStyle);

        saveButton = new Button("Save Recipes");
        saveButton.setStyle(defaultButtonStyle);

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

    private Label recipeNameTitle;
    
    private Label recordTitle;
    private TextField recipeNameField;
    private Button mealTypeButton;
    private Button saveButton;
    private RecipeList recipeList;
    private AppFrame appFrame;
    private Button recordButton;

    // meal type stuff added by dominic
    private String mealType;
    private Label mealTypeTitle;
    private TextField mealTypeField;

    RecipeInputWindow(RecipeList recipeList, AppFrame appFrame) {
        this.recipeList = recipeList;
        this.appFrame = appFrame;

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));


        // recipe name
        recipeNameTitle = new Label("Name:");
        recipeNameTitle.setStyle("-fx-font-weight: bold;");
        layout.getChildren().add(recipeNameTitle);
        
        recipeNameField = new TextField();
        recipeNameField.setPromptText("Enter Recipe Name");
        layout.getChildren().add(recipeNameField);

        // meal type
        mealTypeTitle = new Label("Meal Type:");
        mealTypeTitle.setStyle("-fx-font-weight: bold;");
        layout.getChildren().add(mealTypeTitle);
        
        mealTypeButton = new Button("Select Meal Type");
        mealTypeButton.setOnAction(e -> mealTypeSelector());
        layout.getChildren().add(mealTypeButton);

        // record
        recordTitle = new Label("Record Voice Input:");
        recordTitle.setStyle("-fx-font-weight: bold;");
        layout.getChildren().add(recordTitle);
        
        RecordingAppFrame recorder = new RecordingAppFrame();
        layout.getChildren().add(recorder);
        recorder.setAlignment(Pos.CENTER);

        // save button
        saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveRecipe());
        layout.getChildren().add(saveButton);

        Scene scene = new Scene(layout, 500, 600);
        this.setScene(scene);
        this.setTitle("Add New Recipe");
        this.setResizable(false);
    }

    /*
     * Saves recipe to main app list
     */
    private void saveRecipe() {
        String recipeName = recipeNameField.getText();
        Recipe recipe = new Recipe();
        if (!recipeName.isEmpty()) {

            recipe.getRecipeName().setText(recipeName);
            recipe.setMealType(mealType);
            recipeList.getChildren().add(0, recipe);
            this.close();
        }
        appFrame.addDeleteListener(recipe);
    }

    /*
     * opens external meal type selection window
     */
    private void mealTypeSelector() {
        RecipeMealTypeSelectorWindow recipeMealTypeSelectorWindow = new RecipeMealTypeSelectorWindow(this);
        recipeMealTypeSelectorWindow.show();
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    
    
    private void recordRecipe(){

    }
}
/* select breakfast, lunch, dinner, or enter other (window opens from mealtype button in recipeinputwindow) */
class RecipeMealTypeSelectorWindow extends Stage {

    private Label title;
    private RecipeInputWindow inputWindow;
    private Button breakfastButton;
    private Button lunchButton;
    private Button dinnerButton;
    private Button otherButton;
    private TextField otherField;

    private Button backButton;

    RecipeMealTypeSelectorWindow(RecipeInputWindow inputWindow) {
        this.inputWindow = inputWindow;
        
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        title = new Label("Select Meal Type");
        title.setStyle("-fx-font-weight: bold;");
        layout.getChildren().add(title);

        breakfastButton = new Button("Breakfast");
        breakfastButton.setOnAction(e -> breakfast());
        layout.getChildren().add(breakfastButton);

        lunchButton = new Button("Lunch");
        lunchButton.setOnAction(e -> lunch());
        layout.getChildren().add(lunchButton);

        dinnerButton = new Button("Dinner");
        dinnerButton.setOnAction(e -> dinner());
        layout.getChildren().add(dinnerButton);

        otherButton = new Button("Other (Enter)");
        otherButton.setOnAction(e -> other());
        layout.getChildren().add(otherButton);

        otherField = new TextField();
        otherField.setPromptText("Specify Other Type");
        layout.getChildren().add(otherField);

        Scene scene = new Scene(layout, 500, 600);
        
        this.setScene(scene);
        this.setTitle("Select Meal Type");
        this.setResizable(false);
    }

    private void breakfast() {
        inputWindow.setMealType("breakfast");
        this.close();
    }

    private void lunch() {
        inputWindow.setMealType("lunch");
        this.close();
    }

    private void dinner() {
        inputWindow.setMealType("dinner");
        this.close();
    }

    /* TODO: make new window with just a textfield that allows you to input a custom string for mealtype,
        then do the same setmealtype stuff as in the breakfast(), lunch(), dinner() methods */
    private void other() {
        this.close();
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
        primaryStage.setScene(new Scene(root, 500, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
