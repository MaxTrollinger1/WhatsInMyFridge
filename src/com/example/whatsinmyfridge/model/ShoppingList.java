package com.example.whatsinmyfridge.model;

import java.util.*;

public class ShoppingList {
    private final List<Ingredient> items = new ArrayList<>();

    public List<Ingredient> getItems() {
        return items;
    }

    public void addItem(Ingredient ingredient) {
        // Optional: merge with existing item of same name+unit
        for (Ingredient i : items) {
            if (i.getName().equalsIgnoreCase(ingredient.getName())
                    && i.getUnit().equalsIgnoreCase(ingredient.getUnit())) {
                i.setAmount(i.getAmount() + ingredient.getAmount());
                return;
            }
        }
        items.add(ingredient);
    }

    public void removeItem(Ingredient ingredient) {
        items.remove(ingredient);
    }

    public void clearList() {
        items.clear();
    }

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
