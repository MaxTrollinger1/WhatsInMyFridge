package com.example.whatsinmyfridge.storage.demo;

import com.example.whatsinmyfridge.storage.IDataPersistence;
import com.example.whatsinmyfridge.storage.data.Data;
import com.example.whatsinmyfridge.storage.data.FoodItem;
import com.example.whatsinmyfridge.storage.data.PantryWrapper;

import java.util.ArrayList;
import java.util.Objects;

/// The DemoImplementor Class provides examples of how to utilize
/// the saving interface on scripts that need to have data persistence functionality
public class DemoImplementor implements IDataPersistence {
    private ArrayList<FoodItem> classData = new ArrayList<>();

    public DemoImplementor() {
    }

    /// LOAD EXAMPLE
    @Override
    public void loadData(Data data) {
        // Must check for correct class. All classes receive load call
        if (data instanceof PantryWrapper pantryData) {
            System.out.println("Pantry Information: " + pantryData.toString());
            classData = pantryData.foodItems;
        }
    }

    /// SAVE EXAMPLE
    @Override
    public void saveData(Data data) {
        // Must check for correct class. All classes receive save call
        if (data instanceof PantryWrapper pantryData) {
            System.out.println("Saving PantryWrapper data...");
            pantryData.foodItems = classData;
        }
    }


    //region Debug

    public void WriteNew(FoodItem foodItem)
    {
        classData.add(foodItem);
        System.out.println("Wrote New Food Item");
    }

    //endregion
}