package model;

import org.json.JSONObject;

public class Recipe {
    private String name; // name of the recipe
    private String ingredients; // ingredients that user provides
    private String steps; // the gpt generated recipe (maybe needs a better name)
    private String mealType;
    private String imageURL;
    private String username;

    public Recipe() {
    }

    public Recipe(String name, String mealType, String ingredients, String steps) {
        this.name = name;
        this.mealType = mealType;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public Recipe(String name, String mealType, String ingredients, String steps, String imageURL, String username) {
        this.name = name;
        this.mealType = mealType;
        this.ingredients = ingredients;
        this.steps = steps;
        this.imageURL = imageURL;
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getMealType() {
        return mealType;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Recipe [name=" + name + ", mealType=" + mealType + ", ingredients=" + ingredients + ", steps=" + steps
                + ", imageURL=" + imageURL
                + ", username= " + username + "]";
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public static Recipe fromJSON(JSONObject responseJSON) {
        String name = responseJSON.getString("name");
        String mealType = responseJSON.getString("mealType");
        String ingredients = responseJSON.getString("ingredients");
        String steps = responseJSON.getString("steps");
        String imageURL = responseJSON.getString("imageURL");
        String username = responseJSON.getString("username");

        return new Recipe(name, mealType, ingredients, steps, imageURL, username);
    }

}