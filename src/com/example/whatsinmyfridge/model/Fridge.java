package com.example.whatsinmyfridge.model;

import java.util.*;

/**
 * Singleton class representing the user's fridge inventory.
 * Allows adding, removing, and searching for ingredients.
 */
public class Fridge {
    private static Fridge instance;
    private final List<Ingredient> ingredients = new ArrayList<>();

    /**
     * Private constructor to enforce singleton pattern.
     */
    private Fridge() {}

    /**
     * Returns the singleton instance of the Fridge.
     *
     * @return the Fridge instance
     */
    public static Fridge getInstance() {
        if (instance == null) {
            instance = new Fridge();
        }
        return instance;
    }

    /**
     * Retrieves the list of all ingredients currently in the fridge.
     *
     * @return list of ingredients
     */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Adds a new ingredient to the fridge.
     *
     * @param ingredient the ingredient to add
     */
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    /**
     * Removes an ingredient from the fridge.
     *
     * @param ingredient the ingredient to remove
     */
    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
    }

    /**
     * Finds a matching ingredient by name and unit.
     *
     * @param name the name of the ingredient
     * @param unit the unit of the ingredient
     * @return an Optional containing the matching ingredient if found
     */
    public Optional<Ingredient> findMatchingIngredient(String name, String unit) {
        return ingredients.stream()
                .filter(i -> i.getName().equalsIgnoreCase(name) && i.getUnit().equalsIgnoreCase(unit))
                .findFirst();
    }

    /**
     * Finds all ingredients in the fridge matching a given name, ignoring units.
     *
     * @param name the name to match
     * @return a list of matching ingredients
     */
    public List<Ingredient> getMatchingIngredients(String name) {
        List<Ingredient> matches = new ArrayList<>();
        for (Ingredient i : ingredients) {
            if (i.getName().equalsIgnoreCase(name)) {
                matches.add(i);
            }
        }
        return matches;
    }
}
