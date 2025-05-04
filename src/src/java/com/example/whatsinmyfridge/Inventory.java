package com.example.whatsinmyfridge;

import com.example.whatsinmyfridge.gui.RecipeItem;
import com.example.whatsinmyfridge.model.Ingredient;
import com.example.whatsinmyfridge.model.Recipe;
import com.example.whatsinmyfridge.storage.IDataPersistence;
import com.example.whatsinmyfridge.storage.data.Data;
import com.example.whatsinmyfridge.storage.data.PantryWrapper;
import com.example.whatsinmyfridge.storage.data.RecipeWrapper;

import java.util.ArrayList;
import java.util.List;

public class Inventory implements IDataPersistence {

    public static Inventory instance;

    private ArrayList<Ingredient> ingredients;
    private ArrayList<Recipe> recipes;

    Inventory() {
        ingredients = new ArrayList<>();
        recipes = new ArrayList<>();
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    @Override
    public void loadData(Data data) {
        if (data instanceof PantryWrapper pantryData) {
            System.out.println("Loading PantryWrapper data...");
            this.ingredients = pantryData.foodItems;
        } else if (data instanceof RecipeWrapper recipeData) {
            System.out.println("Loading RecipeWrapper data...");
            this.recipes = recipeData.recipes;
        }
    }

    @Override
    public void saveData(Data data) {
        if (data instanceof PantryWrapper pantryData) {
            System.out.println("Saving PantryWrapper data...");
            pantryData.foodItems = this.ingredients;
        } else if (data instanceof RecipeWrapper recipeData) {
            System.out.println("Saving RecipeWrapper data...");
            recipeData.recipes = this.recipes;
        }
    }
}
