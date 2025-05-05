package com.example.whatsinmyfridge.model;

import java.util.*;

/**
 * Represents a user's wishlist containing recipes they want to make in the future.
 */
public class WishList {
    private final List<Recipe> wishlistedRecipes = new ArrayList<>();

    /**
     * Adds a recipe to the wishlist if it is not already present.
     *
     * @param recipe the recipe to add
     */
    public void addRecipe(Recipe recipe) {
        if (!wishlistedRecipes.contains(recipe)) {
            wishlistedRecipes.add(recipe);
        }
    }

    /**
     * Removes a recipe from the wishlist.
     *
     * @param recipe the recipe to remove
     */
    public void removeRecipe(Recipe recipe) {
        wishlistedRecipes.remove(recipe);
    }

    /**
     * Returns the list of all recipes in the wishlist.
     *
     * @return the list of wishlisted recipes
     */
    public List<Recipe> getRecipes() {
        return wishlistedRecipes;
    }
}
