package model;

public class Recipe {
    private String name; // name of the recipe
    private String ingredients; // ingredients that user provides
    private String steps; // the gpt generated recipe (maybe needs a better name)
    private String mealType;
    private String imageURL;

    public Recipe() {
    }

    public Recipe(String name, String mealType, String ingredients, String steps, String imageURL) {
        this.name = name;
        this.mealType = mealType;
        this.ingredients = ingredients;
        this.steps = steps;
        this.imageURL = imageURL;
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
                + "]";
    }

    public String getImageURL() {
        return this.imageURL;
    }

}
