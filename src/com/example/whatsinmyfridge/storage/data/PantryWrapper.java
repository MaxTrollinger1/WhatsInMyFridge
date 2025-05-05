package com.example.whatsinmyfridge.storage.data;

import com.example.whatsinmyfridge.gui.InventoryItem;
import com.example.whatsinmyfridge.model.Ingredient;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Data wrapper for persisting pantry state.
 * <p>
 * Extends Data to include lists of stocked food items and grocery list items,
 * saved to "pantry.json".
 */
public class PantryWrapper extends Data {

    /**
     * List of ingredients currently in the pantry.
     */
    @Expose
    public ArrayList<Ingredient> foodItems;
    /**
     * List of ingredients on the grocery list.
     */
    @Expose
    public ArrayList<Ingredient> groceryItems;

    /**
     * Default constructor for Gson: initializes empty lists and fileName.
     */
    public PantryWrapper() { // Default constructor for Gson
        this.foodItems = new ArrayList<>();
        this.groceryItems = new ArrayList<>();
        this.fileName = "pantry.json";
    }

    /**
     * Constructs wrapper with timestamp, initializes empty item lists.
     *
     * @param time creation timestamp
     */
    public PantryWrapper(String time) {
        super(time);
        this.time = time;
        this.foodItems = new ArrayList<>();
        this.groceryItems = new ArrayList<>();

        this.fileName = "pantry.json";
    }

    /**
     * Constructs wrapper with timestamp and initial food items.
     * Grocery list remains empty.
     *
     * @param time creation timestamp
     * @param foodItems initial pantry ingredients
     */
    public PantryWrapper(String time, ArrayList<Ingredient> foodItems) {
        super(time);
        this.time = time;
        this.foodItems = foodItems;
    }
}
