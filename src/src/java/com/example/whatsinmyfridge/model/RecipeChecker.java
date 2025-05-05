package com.example.whatsinmyfridge.model;

import com.example.whatsinmyfridge.WhatsInMyFridgeApp;
import java.util.*;

/**
 * Utility class to check if a recipe can be made with the current inventory.
 * Supports unit conversions when needed.
 */
public class RecipeChecker {
    private final UnitConverter converter;

    /**
     * Constructs a RecipeChecker with a specified UnitConverter.
     *
     * @param converter the unit converter to use for ingredient conversions
     */
    public RecipeChecker(UnitConverter converter) {
        this.converter = converter;
    }

    /**
     * Determines whether the specified recipe can be made based on available ingredients.
     * Will attempt unit conversions if direct matches aren't found.
     *
     * @param recipe the recipe to check
     * @return true if the recipe can be made, false otherwise
     */
    public boolean canMakeRecipe(Recipe recipe) {
        for (Ingredient needed : recipe.getIngredients()) {
            double totalAvailable = 0.0;

            // Check if we have matching ingredients
            for (Ingredient owned : WhatsInMyFridgeApp.inventory.getMatchingIngredients(needed.getName())) {
                if (owned.getUnit().equalsIgnoreCase(needed.getUnit())) {
                    totalAvailable += owned.getAmount();
                } else {
                    Optional<Double> converted = converter.convert(
                            needed.getName(), owned.getAmount(), owned.getUnit(), needed.getUnit()
                    );
                    if (converted.isPresent()) {
                        totalAvailable += converted.get();
                    } else {
                        System.out.println("Failed to convert: " + owned.getUnit() + " to " + needed.getUnit());
                    }
                }
            }

            if (totalAvailable < needed.getAmount()) {
                System.out.println("Not enough " + needed.getName() + ". Needed: " +
                        needed.getAmount() + ", Available: " + totalAvailable);
                return false;
            }
        }
        return true;
    }

    /**
     * Filters a list of recipes to return only those that can currently be made.
     *
     * @param allRecipes the list of all recipes
     * @return a list of recipes that are makeable
     */
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
