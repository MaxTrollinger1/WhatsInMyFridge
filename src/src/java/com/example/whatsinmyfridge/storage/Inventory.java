package com.example.whatsinmyfridge.storage;

import com.example.whatsinmyfridge.WhatsInMyFridgeApp;
import com.example.whatsinmyfridge.gui.InventoryItem;
import com.example.whatsinmyfridge.model.Ingredient;
import com.example.whatsinmyfridge.model.Recipe;
import com.example.whatsinmyfridge.storage.data.Data;
import com.example.whatsinmyfridge.storage.data.PantryWrapper;
import com.example.whatsinmyfridge.storage.data.RecipeWrapper;

import java.util.ArrayList;

public class Inventory implements IDataPersistence {

    public static Inventory instance;

    private PantryWrapper pantry;
    private RecipeWrapper recipe;

    public Inventory() {
    }

    //region Mutables

    public void addIngredient(Ingredient ingredient) {
        this.pantry.foodItems.add(ingredient);
    }

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

    public ArrayList<Ingredient> getIngredients() {
        return pantry.foodItems;
    }

    public void addGrocery(Ingredient ingredient) {
        this.pantry.groceryItems.add(ingredient);
    }

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

    public ArrayList<Ingredient> getGroceries() {
        return pantry.groceryItems;
    }


    public void addRecipe(Recipe recipe) {
        this.recipe.recipes.add(recipe);
    }

    public ArrayList<Recipe> getRecipes() {
        if(this.recipe != null)
            return this.recipe.recipes;
        else return new ArrayList<>();
    }

    public void updateRecipeList(ArrayList<Recipe> recipes)
    {
        this.recipe.recipes = recipes;
    }
    //endregion

    //region Data
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
