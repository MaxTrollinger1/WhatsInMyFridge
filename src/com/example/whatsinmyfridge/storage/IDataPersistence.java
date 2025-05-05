package com.example.whatsinmyfridge.storage;

import com.example.whatsinmyfridge.storage.data.*;

/**
 * Interface for persistence-capable classes.
 * Implementers load from and save to Data wrappers (e.g., PantryWrapper, RecipeWrapper).
 */
public interface IDataPersistence {
    /**
     * Populates this object's state from the given data wrapper.
     *
     * @param data wrapper containing persisted state
     */
    void loadData(Data data);

    /**
     * Writes this object's state into the given data wrapper for persistence.
     *
     * @param data wrapper to receive this object's state
     */
    void saveData(Data data);
}