package com.example.whatsinmyfridge.model;

import java.util.*;

/**
 * Utility class to manage unit conversions for ingredients.
 * Supports subscribing for updates and performing unit-to-unit conversions.
 */
public class UnitConverter {
    // Map: Ingredient Name -> (FromUnit -> (ToUnit -> Conversion Ratio))
    private final Map<String, Map<String, Map<String, Double>>> conversions = new HashMap<>();
    private final List<UnitConversionSubscriber> subscribers = new ArrayList<>();

    /**
     * Adds a conversion ratio for a specific ingredient from one unit to another.
     * Also notifies all subscribers that a conversion has been updated.
     *
     * @param ingredient the name of the ingredient
     * @param fromUnit   the source unit
     * @param toUnit     the target unit
     * @param ratio      the conversion ratio (multiply by this to convert)
     */
    public void addConversion(String ingredient, String fromUnit, String toUnit, double ratio) {
        conversions
                .computeIfAbsent(ingredient.toLowerCase(), k -> new HashMap<>())
                .computeIfAbsent(fromUnit.toLowerCase(), k -> new HashMap<>())
                .put(toUnit.toLowerCase(), ratio);

        notifySubscribers(ingredient);
    }

    /**
     * Attempts to convert an ingredient amount from one unit to another.
     *
     * @param ingredient the name of the ingredient
     * @param amount     the amount to convert
     * @param fromUnit   the unit of the provided amount
     * @param toUnit     the desired target unit
     * @return an Optional containing the converted amount if conversion is possible; otherwise, Optional.empty()
     */
    public Optional<Double> convert(String ingredient, double amount, String fromUnit, String toUnit) {
        ingredient = ingredient.toLowerCase();
        fromUnit = fromUnit.toLowerCase();
        toUnit = toUnit.toLowerCase();

        if (!conversions.containsKey(ingredient)) return Optional.empty();
        Map<String, Map<String, Double>> unitMap = conversions.get(ingredient);
        if (!unitMap.containsKey(fromUnit)) return Optional.empty();
        Map<String, Double> targets = unitMap.get(fromUnit);
        if (!targets.containsKey(toUnit)) return Optional.empty();

        double ratio = targets.get(toUnit);
        return Optional.of(amount * ratio);
    }

    /**
     * Subscribes a listener to receive updates when a conversion changes.
     *
     * @param subscriber the subscriber to add
     */
    public void subscribe(UnitConversionSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Notifies all subscribers that a conversion for a specific ingredient has been updated.
     *
     * @param ingredientName the name of the updated ingredient
     */
    private void notifySubscribers(String ingredientName) {
        for (UnitConversionSubscriber subscriber : subscribers) {
            subscriber.onConversionUpdated(ingredientName);
        }
    }
}
