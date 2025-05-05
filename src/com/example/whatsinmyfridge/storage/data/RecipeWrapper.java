package com.example.whatsinmyfridge.storage.data;

import com.example.whatsinmyfridge.gui.RecipeItem;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

import com.example.whatsinmyfridge.model.*;

/**
 * Data wrapper for persisting recipe state.
 * <p>
 * Extends Data to include a list of Recipe objects,
 * saved to "recipe.json".
 */
public class RecipeWrapper extends Data {

    /**
     * List of recipes stored in the application.
     */
    @Expose
    public ArrayList<Recipe> recipes;

    /**
     * Default constructor for Gson: initializes empty list and fileName.
     */
    public RecipeWrapper() { // Default constructor for Gson
        this.recipes = new ArrayList<>();
        this.fileName = "recipe.json";
    }

    /**
     * Constructs wrapper with timestamp, initializes empty recipe list.
     *
     * @param time creation timestamp
     */
    public RecipeWrapper(String time) {
        super(time);
        this.time = time;
        this.recipes = new ArrayList<>();

        this.fileName = "recipe.json";
    }

    /**
     * Constructs wrapper with timestamp and initial recipes.
     *
     * @param time creation timestamp
     * @param recipes initial list of recipes
     */
    public RecipeWrapper(String time, ArrayList<Recipe> foodItems) {
        super(time);
        this.time = time;
        this.recipes = foodItems;
    }

    /**
     * Generates a string representation listing all contained recipes.
     *
     * @return formatted string of recipes
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recipe Wrapper {\n");
        for (Recipe recipe : recipes) {
            sb.append("    ").append(recipe.toString()).append(",\n");
        }
        sb.append("  ]\n");
        sb.append("}");
        return sb.toString();
    }


}
