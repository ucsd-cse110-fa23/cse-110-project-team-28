package model;

import java.util.ArrayList;

public class RecipeData {
    private static RecipeData instance = null;
    private ArrayList<Recipe> recipes;
    private Boolean loaded = false;

    private RecipeData() {
        recipes = new ArrayList<>();
    }

    public static RecipeData getInstance() {
        if (instance == null) {
            instance = new RecipeData();
        }
        return instance;
    }

    public void loadRecipes() {
        if (!loaded) {
            loaded = true;
        }
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
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
                return 0;
            }
        }

        return -1;
    }
}
