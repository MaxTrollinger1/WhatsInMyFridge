package com.example.whatsinmyfridge.storage.data;

import com.example.whatsinmyfridge.gui.RecipeItem;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

import com.example.whatsinmyfridge.model.*;

/// Wrapper for food saving data
public class RecipeWrapper extends Data {

    @Expose
    public ArrayList<Recipe> recipes;

    public RecipeWrapper() { // Default constructor for Gson
        this.recipes = new ArrayList<>();
        this.fileName = "recipe.json";
    }

    public RecipeWrapper(String time) {
        super(time);
        this.time = time;
        this.recipes = new ArrayList<>();

        this.fileName = "recipe.json";
    }

    public RecipeWrapper(String time, ArrayList<Recipe> foodItems) {
        super(time);
        this.time = time;
        this.recipes = foodItems;
    }

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
