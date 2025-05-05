package com.example.whatsinmyfridge.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main class to demonstrate the usage of the fridge and unit converter functionality.
 */
public class Main {

    /**
     * Entry point of the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // Create and configure the unit converter
        UnitConverter converter = new UnitConverter();
        converter.addConversion("Flour", "gram", "cup", 0.0042);
        converter.addConversion("Flour", "cup", "gram", 240.0);

        // Add ingredients to the fridge
        Fridge fridge = Fridge.getInstance();
        fridge.addIngredient(new Ingredient("Flour", 150.0, "gram"));
        fridge.addIngredient(new Ingredient("Egg", 2.0, "pcs"));

        // Define ingredients needed for a pancake recipe
        List<Ingredient> pancakeIngredients = Arrays.asList(
                new Ingredient("Flour", 1.0, "cup"),
                new Ingredient("Egg", 2.0, "pcs")
        );

        /*
        Example of creating and working with a recipe:
        
        Recipe pancake = new Recipe("Pancakes", "Basic pancake recipe", pancakeIngredients);
        fridge.addIngredient(new Ingredient("Flour", 2000.0, "gram"));

        WishList wishlist = new WishList();
        wishlist.addRecipe(pancake);

        RecipeChecker checker = new RecipeChecker(converter);
        System.out.println("Can make pancakes: " + checker.canMakeRecipe(pancake));

        List<Recipe> allRecipes = new ArrayList<>();
        allRecipes.add(pancake);
        List<Recipe> available = checker.getMakeableRecipes(allRecipes);
        System.out.println("Makeable recipes: " + available.size());
        */
    }
}
