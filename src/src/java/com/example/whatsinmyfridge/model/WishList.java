package com.example.whatsinmyfridge.model;

import java.util.*;

public class WishList {
    private final List<Recipe> wishlistedRecipes = new ArrayList<>();

    public void addRecipe(Recipe recipe) {
        if (!wishlistedRecipes.contains(recipe)) {
            wishlistedRecipes.add(recipe);
        }
    }

    public void removeRecipe(Recipe recipe) {
        wishlistedRecipes.remove(recipe);
    }

    public List<Recipe> getRecipes() {
        return wishlistedRecipes;
    }
}
