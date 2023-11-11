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

import controllers.MainController;
import controllers.RecipeController;
import multithreading.RecordingAppFrame;

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

        Font customFont = Font.loadFont(getClass().getResourceAsStream("fonts/Roboto-Medium.ttf"),
                12);

        addButton = new Button();
        addButton.getStyleClass().add("add-button");
        addButton.setText("Add Recipe");
        addButton.setFont(customFont);

        Image addImage = new Image(
                getClass().getResourceAsStream("images/add.png"));
        addButton.setGraphic(new ImageView(addImage));

        saveButton = new Button();
        saveButton.getStyleClass().add("save-button");
        saveButton.setText("Save Recipes");
        saveButton.setFont(customFont);

        // using download icon for save button
        Image saveImage = new Image(getClass().getResourceAsStream("images/download.png"));
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

        Font customFont = Font.loadFont(getClass().getResourceAsStream("fonts/Roboto-Black.ttf"),
                24);

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
 * Class for the seperate window for user to enter recipe parameters (meal type,
 * ingredients) for recipe creation
 */
class RecipeInputWindow extends Stage {

    public static final String ERROR_FLAG = "ERROR";
    private final String promptTemplate = "" + //
            "Generate a [mealType] recipe using the following ingredients only:[listOfIngredients]. " + //
            "Please include list of ingredients, preparation instructions, and numbered cooking steps. " + //
            "Place title of recipe on first line of your reponse. \n" + //
            "\n" + //
            "Meal Type: [mealType]\n" + //
            "Ingredients: [listOfIngredients]\n" + //
            "\n" + //
            "Recipe:";

    private Label recordTitle;
    private TextField recipeNameField;

    private Button saveButton;
    private RecipeList recipeList;
    private AppFrame appFrame;

    // button to signal that user is done recording and start transcription
    // Find a better solution to
    private Button ingredientsDoneButton;
    private Button mealTypeDoneButton;

    // meal type stuff added by dominic
    private String mealType;
    private Label mealTypeTitle;

    private Button breakfastButton;
    private Button lunchButton;
    private Button dinnerButton;

    private TextField ingredientsField;
    private TextArea stepsField;
    private TextField mealTypeField;

    RecipeInputWindow(RecipeList recipeList, AppFrame appFrame) {
        this.recipeList = recipeList;
        this.appFrame = appFrame;

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        // recipe name
        Label recipeNameTitle = new Label("Name:");
        recipeNameTitle.setStyle("-fx-font-weight: bold;");
        layout.getChildren().add(recipeNameTitle);

        recipeNameField = new TextField();
        recipeNameField.setPromptText("Enter Recipe Name");
        layout.getChildren().add(recipeNameField);

        // meal type
        mealTypeTitle = new Label("Meal Type:");
        mealTypeTitle.setStyle("-fx-font-weight: bold;");
        layout.getChildren().add(mealTypeTitle);
        /*
         * breakfastButton = new Button("Breakfast");
         * breakfastButton.setOnAction(e -> setMealType("Breakfast"));
         * layout.getChildren().add(breakfastButton);
         * 
         * lunchButton = new Button("Lunch");
         * lunchButton.setOnAction(e -> setMealType("Lunch"));
         * layout.getChildren().add(lunchButton);
         * 
         * dinnerButton = new Button("Dinner");
         * dinnerButton.setOnAction(e -> setMealType("Dinner"));
         * layout.getChildren().add(dinnerButton);
         */

        mealTypeField = new TextField();
        mealTypeField.setText("Meal Type");

        RecordingAppFrame mealTypeRecorder = new RecordingAppFrame("mealType.wav");
        mealTypeRecorder.setAlignment(Pos.CENTER);

        mealTypeDoneButton = new Button("Done");
        mealTypeDoneButton.setOnAction(e -> {
            String mealTypeResult = getRecordingTranscript("mealType.wav");
            if (mealTypeResult.equals(ERROR_FLAG)) {
                System.out.println("An error occurred while getting meal type"); // maybe throw an exception instead
            } else {
                mealTypeField.setText(mealTypeResult);
                setMealType(mealTypeField.getText());
            }
        });
        layout.getChildren().addAll(mealTypeRecorder, mealTypeDoneButton, mealTypeField);

        // record
        recordTitle = new Label("ingredients:");
        recordTitle.setStyle("-fx-font-weight: bold;");
        layout.getChildren().add(recordTitle);

        RecordingAppFrame ingredientsRecorder = new RecordingAppFrame("ingredients.wav");
        layout.getChildren().add(ingredientsRecorder);
        ingredientsRecorder.setAlignment(Pos.CENTER);

        ingredientsField = new TextField();
        ingredientsField.setText("Ingredients");

        stepsField = new TextArea();
        stepsField.setText("steps");
        stepsField.setPrefWidth(300);
        stepsField.setPrefHeight(800);

        ingredientsDoneButton = new Button("Done");
        ingredientsDoneButton.setOnAction(e -> {
            String ingredientsResult = getRecordingTranscript("ingredients.wav");
            if (ingredientsResult.equals(ERROR_FLAG)) {
                System.out.println("An error occurred while getting ingredients"); // maybe throw an exception instead
            } else {
                ingredientsField.setText(ingredientsResult);
                String recipeResults;
                recipeResults = getRecipeSteps();
                if (recipeResults.equals(ERROR_FLAG)) {
                    System.out.println("An error occured while getting steps");
                } else {
                    stepsField.setText(recipeResults);
                    recipeNameField.setText(getRecipeName(recipeResults));
                }
            }
        });
        layout.getChildren().addAll(ingredientsDoneButton, ingredientsField, stepsField);

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
            recipe.setIngredients(ingredientsField.getText());
            recipe.setSteps(stepsField.getText());
            recipeList.getChildren().add(0, recipe);
            this.close();
        }
        appFrame.addDeleteListener(recipe);
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getRecordingTranscript(String fileName) {
        try {
            return Whisper.getWhisperTranscript(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An I/O error occurred: " + e.getMessage());
            return ERROR_FLAG;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Invalid URI: Check file path.");
            return ERROR_FLAG;
        }
    }

    public String getRecipeSteps() {
        try {
            return ChatGPT.getGPTResponse(500, promptTemplate.replace("[mealType]", mealType)
                    .replace("[listOfIngredients]", ingredientsField.getText()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An I/O error occurred: " + e.getMessage());
            return ERROR_FLAG;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Invalid URI: Check file path.");
            return ERROR_FLAG;
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("InterruptedException");
            return ERROR_FLAG;
        }
    }

    static public String getRecipeName(String steps) {
        String[] lines = steps.split("\n");
        return lines[0].trim();
    }

    public String getIngredients() {
        return ingredientsField.getText();
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

    final int WIDTH = 500;
    final int HEIGHT = 500;
    final int WINDOW_PADDING = 10;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));
        Parent root = loader.load();
        MainController mainController = loader.getController();

        // debugging
        // mainController.addRecipe("Chicken");
        // mainController.addRecipe("Bananas");
        // mainController.addRecipe("Apples");
        // mainController.addRecipe("Oranges");
        // mainController.addRecipe("Pears");
        // mainController.addRecipe("Peaches");
        // mainController.addRecipe("Pineapples");
        // mainController.addRecipe("Strawberries");
        // mainController.addRecipe("Blueberries");
        // mainController.addRecipe("Raspberries");
        // mainController.addRecipe("Blackberries");
        // mainController.addRecipe("Mangoes");

        primaryStage.setTitle("PantryPal");

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(true);

        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMinHeight(HEIGHT);

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
