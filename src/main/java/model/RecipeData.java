package model;

import java.util.ArrayList;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class RecipeData {
    private final static String DEFAULT_FILE = "recipes.json";
    private static RecipeData instance = null;

    private String fileLocation;
    private ArrayList<Recipe> recipes;

    public static RecipeData getInstance() {
        if (instance == null) {
            instance = new RecipeData();
            instance.setFileLocation(DEFAULT_FILE);
        }

        return instance;
    }

    public RecipeData() {
        this.recipes = new ArrayList<Recipe>();
    }

    public static void setInstance(RecipeData recipeData) {
        instance = recipeData;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
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

    public void clear() {
        recipes.clear();
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

    /**
     * Method to load recipes from a file
     */
    public void loadRecipes() {
        // use Gson to convert recipes to JSON
        Gson gson = new Gson();

        // read JSON from file
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLocation));

            // convert JSON to Recipe array
            Type recipeListType = new TypeToken<ArrayList<Recipe>>() {
            }.getType();

            ArrayList<Recipe> recipes = gson.fromJson(bufferedReader, recipeListType);

            if (recipes != null)
                // add recipes to RecipeData
                setRecipes(recipes);

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to save recipes to a file
     */
    public void saveRecipes() {
        // use Gson to convert recipes to JSON
        Gson gson = new Gson();

        try {
            FileWriter fileWriter = new FileWriter(fileLocation);

            gson.toJson(getRecipes(), fileWriter);

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
