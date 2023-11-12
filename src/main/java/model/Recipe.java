package model;

public class Recipe {
    private String name; // name of the recipe
    private String ingredients; // ingredients that user provides
    private String steps; // the gpt generated recipe (maybe needs a better name)
    private String mealType;

    public Recipe() {
    }

    public Recipe(String name, String mealType, String ingredients, String steps) {
        this.name = name;
        this.mealType = mealType;
        this.ingredients = ingredients;
        this.steps = steps;
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
                + "]";
    }

}
