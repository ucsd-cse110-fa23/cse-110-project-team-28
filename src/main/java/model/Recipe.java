package model;

import org.bson.Document;
import org.json.JSONObject;

import java.util.Date;

public class Recipe {
    private String name; // name of the recipe
    private String mealType;
    private String ingredients; // ingredients that user provides
    private String steps; // the gpt generated recipe (maybe needs a better name)
    private String imageURL;

    // provided by the server
    private String id; // referenced as _id in the database

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

    public String getImageURL() {
        return this.imageURL;
    }

    public String getId() {
        return id;
    }

    public Recipe setName(String name) {
        this.name = name;
        return this;
    }

    public Recipe setMealType(String mealType) {
        this.mealType = mealType;
        return this;
    }

    public Recipe setIngredients(String ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public Recipe setSteps(String steps) {
        this.steps = steps;
        return this;
    }

    public Recipe setImageUrl(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public Recipe setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "Recipe [name=" + name + ", mealType=" + mealType + ", ingredients=" + ingredients + ", steps=" + steps
                + ", imageURL=" + imageURL
                + ", id=" + id + "]";
    }

    public static Recipe fromJSON(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String mealType = jsonObject.getString("mealType");
        String ingredients = jsonObject.getString("ingredients");
        String steps = jsonObject.getString("steps");
        String imageURL = jsonObject.getString("imageURL");
        String id = jsonObject.getString("_id");

        return new Recipe()
                .setName(name)
                .setMealType(mealType)
                .setIngredients(ingredients)
                .setSteps(steps)
                .setImageUrl(imageURL)
                .setId(id);
    }

    /**
     * Converts a Recipe object to a MongoDB Document. If Document is null, returns
     * null.
     * 
     * @param document
     * @return
     */
    public static Recipe fromDocument(Document document) {
        if (document == null)
            return null;

        String name = document.getString("name");
        String mealType = document.getString("mealType");
        String ingredients = document.getString("ingredients");
        String steps = document.getString("steps");
        String imageURL = document.getString("imageURL");
        String id = document.getObjectId("_id").toString();

        Recipe recipe = new Recipe()
                .setName(name)
                .setMealType(mealType)
                .setIngredients(ingredients)
                .setSteps(steps)
                .setImageUrl(imageURL)
                .setId(id);

        return recipe;
    }

}
