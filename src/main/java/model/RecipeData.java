package model;

import java.util.ArrayList;

public class RecipeData {
    private static RecipeData instance = null;
    private ArrayList<Recipe> recipes;

    private RecipeData() {
        recipes = new ArrayList<>();
    }

    public static RecipeData getInstance() {
        if (instance == null) {
            instance = new RecipeData();
        }
        return instance;
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }
}
