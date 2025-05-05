package com.example.whatsinmyfridge.model;

import java.util.*;

/**
 * Represents a shopping list containing ingredients the user needs to buy.
 */
public class ShoppingList {
    private final List<Ingredient> items = new ArrayList<>();

    /**
     * Returns the list of ingredients currently in the shopping list.
     *
     * @return the list of ingredients
     */
    public List<Ingredient> getItems() {
        return items;
    }

    /**
     * Adds an ingredient to the shopping list.
     * If an ingredient with the same name and unit already exists, it increases its amount.
     *
     * @param ingredient the ingredient to add
     */
    public void addItem(Ingredient ingredient) {
        for (Ingredient i : items) {
            if (i.getName().equalsIgnoreCase(ingredient.getName())
                    && i.getUnit().equalsIgnoreCase(ingredient.getUnit())) {
                i.setAmount(i.getAmount() + ingredient.getAmount());
                return;
            }
        }
        items.add(ingredient);
    }

    /**
     * Removes an ingredient from the shopping list.
     *
     * @param ingredient the ingredient to remove
     */
    public void removeItem(Ingredient ingredient) {
        items.remove(ingredient);
    }

    /**
     * Clears all ingredients from the shopping list.
     */
    public void clearList() {
        items.clear();
    }

    /**
     * Generates a shopping list based on missing ingredients needed for a recipe.
     * Considers the current contents of the fridge and uses unit conversion when needed.
     *
     * @param recipe    the recipe to generate a shopping list for
     * @param converter the unit converter to use for unit matching
     */
    public void generateFromRecipe(Recipe recipe, UnitConverter converter) {
        Fridge fridge = Fridge.getInstance();
        for (Ingredient needed : recipe.getIngredients()) {
            double totalAvailable = 0.0;

            for (Ingredient owned : fridge.getMatchingIngredients(needed.getName())) {
                if (owned.getUnit().equalsIgnoreCase(needed.getUnit())) {
                    totalAvailable += owned.getAmount();
                } else {
                    Optional<Double> converted = converter.convert(
                            needed.getName(), owned.getAmount(), owned.getUnit(), needed.getUnit());
                    if (converted.isPresent()) {
                        totalAvailable += converted.get();
                    }
                }
            }

            if (totalAvailable < needed.getAmount()) {
                double missingAmount = needed.getAmount() - totalAvailable;
                addItem(new Ingredient(
                        needed.getName(),
                        missingAmount,
                        needed.getUnit()
                ));
            }
        }
    }
}
