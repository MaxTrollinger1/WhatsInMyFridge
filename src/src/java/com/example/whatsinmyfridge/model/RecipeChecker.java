package com.example.whatsinmyfridge.model;

import com.example.whatsinmyfridge.WhatsInMyFridgeApp;

import java.util.*;

public class RecipeChecker {
    private final UnitConverter converter;

    public RecipeChecker(UnitConverter converter) {
        this.converter = converter;
    }

    public boolean canMakeRecipe(Recipe recipe) {

        // Loop through all ingredients required for the recipe
        for (Ingredient needed : recipe.getIngredients()) {
            double totalAvailable = 0.0;

            // Check if we have any matching ingredients in the inventory
            for (Ingredient owned : WhatsInMyFridgeApp.inventory.getMatchingIngredients(needed.getName())) {

                // If the units match, add the owned amount to the total
                if (owned.getUnit().equalsIgnoreCase(needed.getUnit())) {
                    totalAvailable += owned.getAmount();
                } else {
                    // If the units don't match, attempt to convert
                    Optional<Double> converted = converter.convert(
                            needed.getName(), owned.getAmount(), owned.getUnit(), needed.getUnit()
                    );

                    // If the conversion is successful, add the converted amount
                    if (converted.isPresent()) {
                        totalAvailable += converted.get();
                    } else {
                        // If conversion fails, print a message for debugging
                        System.out.println("Failed to convert: " + owned.getUnit() + " to " + needed.getUnit());
                    }
                }
            }

            // Check if we have enough of the ingredient
            if (totalAvailable < needed.getAmount()) {
                // Print out details of why we can't make the recipe
                System.out.println("Not enough " + needed.getName() + ". Needed: " + needed.getAmount() + ", Available: " + totalAvailable);
                return false;
            }
        }

        // If we have all the required ingredients, we can make the recipe
        return true;
    }


    public List<Recipe> getMakeableRecipes(List<Recipe> allRecipes) {
        List<Recipe> makeable = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (canMakeRecipe(recipe)) {
                makeable.add(recipe);
            }
        }
        return makeable;
    }
}
