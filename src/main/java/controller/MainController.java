package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.text.RandomStringGenerator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Recipe;
import model.RecipeData;

public class MainController implements Initializable {

    @FXML
    private VBox recipeList;

    @FXML
    private Label noRecipesLabel;

    @FXML
    private Button addRecipeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // bind managed property of noRecipesLabel to visible property of noRecipesLabel
        noRecipesLabel.managedProperty().bind(noRecipesLabel.visibleProperty());
        // set noRecipesLabel to visible
        noRecipesLabel.setVisible(true);

        this.updateRecipeList();
    }

    private void updateRecipeList() {
        // clear recipeList
        recipeList.getChildren().clear();

        RecipeData recipeData = RecipeData.getInstance();

        for (Recipe recipe : recipeData.getRecipes()) {
            try {
                addRecipe(recipe);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds a recipe to the recipeList
     * 
     * @param recipeName the name of the recipe to add
     * @throws IOException
     */
    public void addRecipe(Recipe recipe) throws IOException {
        // set noRecipesLabel to invisible
        noRecipesLabel.setVisible(false);

        // load recipePane.fxml and get its RecipeController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/recipePane.fxml"));
        GridPane recipePane = loader.load();
        RecipePaneController recipePaneController = loader.getController();

        // set recipeName
        recipePaneController.setRecipe(recipe);

        // add recipePane to recipeList
        recipeList.getChildren().add(recipePane);
    }

    /**
     * Adds recipes to the recipeList
     * 
     * @param recipeNames the names of the recipes to add
     * @throws IOException
     */
    public void addRecipes(Recipe[] recipes) throws IOException {
        for (Recipe recipe : recipes) {
            addRecipe(recipe);
        }
    }

    public void addRecipeHandler() throws IOException {
        Scene scene = addRecipeButton.getScene();
        Stage stage = (Stage) scene.getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/newRecipe.fxml"));
        Scene newScene = new Scene(root, scene.getWidth(), scene.getHeight());

        stage.setScene(newScene);
        stage.show();
    }

    public void debugAddRecipeHandler() throws IOException {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        Recipe recipe = new Recipe(generator.generate(10), "Dinner",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        RecipeData.getInstance().addRecipe(recipe);

        updateRecipeList();
    }
}
