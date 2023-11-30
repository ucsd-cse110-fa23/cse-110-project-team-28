package model;

import java.util.ArrayList;

public class RecipeData {
    private static RecipeData instance = new RecipeData();
    private ArrayList<Recipe> recipes;

    public static RecipeData getInstance() {
        return instance;
    }

    public RecipeData() {
        this.recipes = new ArrayList<Recipe>();
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void clear() {
        recipes.clear();
    }
}
