package com.example.whatsinmyfridge.storage.data;

import com.example.whatsinmyfridge.model.Ingredient;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/// Wrapper for food saving data
public class PantryWrapper extends Data {

    @Expose
    public ArrayList<Ingredient> foodItems;

    public PantryWrapper() { // Default constructor for Gson
        this.foodItems = new ArrayList<>();
        this.fileName = "pantry.json";
    }

    public PantryWrapper(String time) {
        super(time);
        this.time = time;
        this.foodItems = new ArrayList<>();

        this.fileName = "pantry.json";
    }

    public PantryWrapper(String time, ArrayList<Ingredient> foodItems) {
        super(time);
        this.time = time;
        this.foodItems = foodItems;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PantryWrapper {\n");
        sb.append("  time: ").append(time).append(",\n");
        sb.append("  fileName: ").append(fileName).append(",\n");
        sb.append("  foodItems: [\n");
        for (Ingredient foodItem : foodItems) {
            sb.append("    ").append(foodItem.toString()).append(",\n");
        }
        sb.append("  ]\n");
        sb.append("}");
        return sb.toString();
    }


}
