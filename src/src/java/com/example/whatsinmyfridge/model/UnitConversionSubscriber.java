package com.example.whatsinmyfridge.model;

/**
 * Interface for classes that want to subscribe to unit conversion updates.
 */
public interface UnitConversionSubscriber {

    /**
     * Callback method invoked when a conversion for a specific ingredient is updated.
     *
     * @param ingredientName the name of the ingredient whose conversion was updated
     */
    void onConversionUpdated(String ingredientName);
}
