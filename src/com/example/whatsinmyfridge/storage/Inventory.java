package com.example.whatsinmyfridge.storage;

import com.example.whatsinmyfridge.WhatsInMyFridgeApp;
import com.example.whatsinmyfridge.gui.InventoryItem;
import com.example.whatsinmyfridge.model.Ingredient;
import com.example.whatsinmyfridge.model.Recipe;
import com.example.whatsinmyfridge.storage.data.Data;
import com.example.whatsinmyfridge.storage.data.PantryWrapper;
import com.example.whatsinmyfridge.storage.data.RecipeWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages the application's inventory, including pantry and recipe lists.
 * Implements IDataPersistence to load and save PantryWrapper and RecipeWrapper data.
 */
public class Inventory implements IDataPersistence {

    /** Singleton instance, set by DataPersistenceManager. */
    public static Inventory instance;

    private PantryWrapper pantry;
    private RecipeWrapper recipe;

    /**
     * Creates a new Inventory; wrappers are initialized via loadData.
     */
    public Inventory() {
    }

    //region Mutables

    /**
     * Adds an ingredient to the pantry list.
     * @param ingredient item to add
     */
    public void addIngredient(Ingredient ingredient) {
        this.pantry.foodItems.add(ingredient);
    }

    /**
     * Removes a pantry ingredient by name.
     * @param name ingredient name
     * @return true if removed, false if not found
     */
    public boolean removeIngredient(String name)
    {
        for (Ingredient ingredient : this.pantry.foodItems) {
            if (ingredient.getName().equals(name)) {
                this.pantry.foodItems.remove(ingredient);
                return true;
            }
        }
        return false;
    }

    /**
     * Updates an existing pantry ingredient.
     * @param value updated ingredient (matched by name)
     * @return true if updated, false if not found
     */
    public boolean editIngredient(Ingredient value)
    {
        for (int i = 0; i < this.pantry.foodItems.size(); i++) {
            Ingredient ingredient = this.pantry.foodItems.get(i);
            if (ingredient.getName().equals(value.getName())) {
                this.pantry.foodItems.set(i, value);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all pantry ingredients.
     * @return list of pantry items
     */
    public ArrayList<Ingredient> getIngredients() {
        return pantry.foodItems;
    }

    /**
     * Adds an ingredient to the grocery list.
     * @param ingredient item to add
     */
    public void addGrocery(Ingredient ingredient) {
        this.pantry.groceryItems.add(ingredient);
    }

    /**
     * Removes a grocery item by name.
     * @param name item name
     * @return true if removed, false if not found
     */
    public boolean removeGrocery(String name)
    {
        for (Ingredient ingredient : this.pantry.groceryItems) {
            if (ingredient.getName().equals(name)) {
                this.pantry.groceryItems.remove(ingredient);
                return true;
            }
        }
        return false;
    }

    /**
     * Updates an existing grocery item.
     * @param value updated item (matched by name)
     * @return true if updated, false if not found
     */
    public boolean editGrocery(Ingredient value)
    {
        for (int i = 0; i < this.pantry.groceryItems.size(); i++) {
            Ingredient ingredient = this.pantry.groceryItems.get(i);
            if (ingredient.getName().equals(value.getName())) {
                this.pantry.groceryItems.set(i, value);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all items on the grocery list.
     * @return list of grocery items
     */
    public ArrayList<Ingredient> getGroceries() {
        return pantry.groceryItems;
    }

    /**
     * Adds a recipe to inventory.
     * @param recipe recipe to add
     */
    public void addRecipe(Recipe recipe) {
        this.recipe.recipes.add(recipe);
    }

    /**
     * Returns all stored recipes.
     * @return list of recipes (empty if none)
     */
    public ArrayList<Recipe> getRecipes() {
        if(this.recipe != null)
            return this.recipe.recipes;
        else return new ArrayList<>();
    }

    /**
     * Removes a recipe by name.
     * @param name recipe name
     * @return true if removed, false if not found
     */
    public boolean removeRecipe(String name) {
        for (Recipe r : this.recipe.recipes) {
            if (r.getName().equals(name)) {
                this.recipe.recipes.remove(r);
                return true;
            }
        }
        return false;
    }

    /**
     * Replaces the current recipe list.
     * @param recipes new list of recipes
     */
    public void updateRecipeList(ArrayList<Recipe> recipes)
    {
        this.recipe.recipes = recipes;
    }
    //endregion

    //region Logic

    /**
     * Finds a pantry ingredient matching name and unit.
     * @param name ingredient name
     * @param unit measurement unit
     * @return optional matching ingredient
     */
    public Optional<Ingredient> findMatchingIngredient(String name, String unit) { // unit should be optional
        return pantry.foodItems.stream()
                .filter(i -> i.getName().equalsIgnoreCase(name) && i.getUnit().equalsIgnoreCase(unit))
                .findFirst();
    }

    /**
     * Finds all pantry ingredients with a given name.
     * @param name ingredient name
     * @return list of matching ingredients
     */
    public List<Ingredient> getMatchingIngredients(String name) {
        List<Ingredient> matches = new ArrayList<>();
        for (Ingredient i : pantry.foodItems) {
            if (i.getName().equalsIgnoreCase(name)) {
                matches.add(i);
            }
        }
        return matches;
    }


    //endregion

    //region Data

    /**
     * Loads PantryWrapper or RecipeWrapper data into this inventory.
     * Triggers UI update via WhatsInMyFridgeApp.onLoadedData().
     * @param data wrapper containing pantry or recipe lists
     */
    @Override
    public void loadData(Data data) {
        if (data instanceof PantryWrapper pantryData) {
            System.out.println("Loading PantryWrapper data...");
            this.pantry = pantryData;
        } else if (data instanceof RecipeWrapper recipeData) {
            System.out.println("Loading RecipeWrapper data...");
            this.recipe = recipeData;
        }

        // Update Loaded
        WhatsInMyFridgeApp.instance.onLoadedData();
    }

    /**
     * Saves current pantry or recipe state back into the provided wrapper.
     * @param data wrapper to populate and persist
     */
    @Override
    public void saveData(Data data) {
        if (data instanceof PantryWrapper pantryData) {
            System.out.println("Saving PantryWrapper data...");
            pantryData = this.pantry;
        } else if (data instanceof RecipeWrapper recipeData) {
            System.out.println("Saving RecipeWrapper data...");
            recipeData = this.recipe;
        }
    }

    //endregion
}
