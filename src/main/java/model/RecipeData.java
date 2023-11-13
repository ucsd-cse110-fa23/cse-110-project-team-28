package model;

import java.util.ArrayList;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class RecipeData implements Observable {
    private final static String recipeFilePath = "recipes.json";

    private static RecipeData instance = null;

    private ArrayList<Recipe> recipes;
    private Observer observer;

    private RecipeData() {
        recipes = new ArrayList<>();
    }

    public static RecipeData getInstance() {
        if (instance == null) {
            instance = new RecipeData();
        }
        return instance;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        saveRecipes();
    }

    /**
     * Deletes a recipe from the RecipeData
     * 
     * @param recipe the recipe to delete
     * @return 0 if successful, -1 if not
     */
    public int deleteRecipe(Recipe recipe) {
        for (int i = 0; i < recipes.size(); i++) {
            if (recipes.get(i).equals(recipe)) {
                recipes.remove(i);
                saveRecipes();
                return 0;
            }
        }

        return -1;
    }

    /**
     * Deletes a recipe from the RecipeData
     * 
     * @param recipeName the name of the recipe to delete
     * @return 0 if successful, -1 if not
     */
    public int deleteRecipe(String recipeName) {
        for (int i = 0; i < recipes.size(); i++) {
            if (recipes.get(i).getName().equals(recipeName)) {
                recipes.remove(i);
                saveRecipes();
                return 0;
            }
        }

        return -1;
    }

    /**
     * Updates a recipe in the RecipeData
     * 
     * @param recipe the recipe to update
     * @return 0 if successful, -1 if not
     */
    public int updateRecipe(Recipe recipe) {
        for (int i = 0; i < recipes.size(); i++) {
            if (recipes.get(i).getName().equals(recipe.getName())) {
                recipes.set(i, recipe);
                saveRecipes();
                return 0;
            }
        }

        return -1;
    }

    public void loadRecipes() {
        loadRecipes(recipeFilePath);
    }

    // Method to load recipes from a file
    public void loadRecipes(String recipeFilePath) {
        // Get all recipes from RecipeData
        RecipeData recipeData = RecipeData.getInstance();

        // use Gson to convert recipes to JSON
        Gson gson = new Gson();

        // read JSON from file
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(recipeFilePath));

            // convert JSON to Recipe array
            Type recipeListType = new TypeToken<ArrayList<Recipe>>() {
            }.getType();

            ArrayList<Recipe> recipes = gson.fromJson(bufferedReader, recipeListType);

            // add recipes to RecipeData
            recipeData.setRecipes(recipes);

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        notifyObservers();
    }

    public void saveRecipes() {
        saveRecipes(recipeFilePath);
    }

    // Method to save recipes to a file
    public void saveRecipes(String recipeFilePath) {
        // Get all recipes from RecipeData
        RecipeData recipeData = RecipeData.getInstance();

        // use Gson to convert recipes to JSON
        Gson gson = new Gson();

        try {
            // write JSON to file
            FileWriter fileWriter = new FileWriter(recipeFilePath);

            gson.toJson(recipeData.getRecipes(), fileWriter);

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    @Override
    public void notifyObservers() {
        if (observer != null)
            observer.update();
    }
}
