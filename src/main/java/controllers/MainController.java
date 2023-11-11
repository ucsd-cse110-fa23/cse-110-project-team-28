package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MainController implements Initializable {

    @FXML
    private VBox recipeList;

    @FXML
    private Label noRecipesLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // bind prefHeight of noRecipesLabel to height of parent recipeList
        noRecipesLabel.prefHeightProperty().bind(recipeList.heightProperty());
        // bind managed property of noRecipesLabel to visible property of noRecipesLabel
        noRecipesLabel.managedProperty().bind(noRecipesLabel.visibleProperty());
        // set noRecipesLabel to visible
        noRecipesLabel.setVisible(true);
    }

    /**
     * Adds a recipe to the recipeList
     * 
     * @param recipeName the name of the recipe to add
     * @throws IOException
     */
    public void addRecipe(String recipeName) throws IOException {
        // set noRecipesLabel to invisible
        noRecipesLabel.setVisible(false);

        // load recipe.fxml and get its RecipeController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/recipe.fxml"));
        GridPane recipePane = loader.load();
        RecipeController recipeController = loader.getController();

        // set recipeName
        recipeController.setRecipeName(recipeName);

        // add recipePane to recipeList
        recipeList.getChildren().add(recipePane);
    }

    /**
     * Adds recipes to the recipeList
     * 
     * @param recipeNames the names of the recipes to add
     * @throws IOException
     */
    public void addRecipes(String[] recipeNames) throws IOException {
        for (String recipeName : recipeNames) {
            addRecipe(recipeName);
        }
    }
}
