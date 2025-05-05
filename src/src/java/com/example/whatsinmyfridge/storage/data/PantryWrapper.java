package com.example.whatsinmyfridge.storage.data;

import com.example.whatsinmyfridge.gui.InventoryItem;
import com.example.whatsinmyfridge.model.Ingredient;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/// Wrapper for food saving data
public class PantryWrapper extends Data {

    @Expose
    public ArrayList<Ingredient> foodItems;
    @Expose
    public ArrayList<Ingredient> groceryItems;

    public PantryWrapper() { // Default constructor for Gson
        this.foodItems = new ArrayList<>();
        this.groceryItems = new ArrayList<>();
        this.fileName = "pantry.json";
    }

    public PantryWrapper(String time) {
        super(time);
        this.time = time;
        this.foodItems = new ArrayList<>();
        this.groceryItems = new ArrayList<>();

        this.fileName = "pantry.json";
    }

    public PantryWrapper(String time, ArrayList<Ingredient> foodItems) {
        super(time);
        this.time = time;
        this.foodItems = foodItems;
    }
}
