package com.example.whatsinmyfridge.utils;

import java.util.Random;

/**
 * Used for testing purposes.
 */
public class FoodNameGenerator {

    private static final String[] FOOD_NAMES = {
            "Apple",
            "Banana",
            "Carrot",
            "Milk",
            "Cheese",
            "Bread",
            "Chicken",
            "Fish",
            "Rice",
            "Tomato",
            "Potato",
            "Orange",
            "Lettuce",
            "Yogurt",
            "Eggs"
    };

    private static final Random RANDOM = new Random();

    /**
     * Generates and returns a random food name.
     *
     * @return A randomly selected food name from the list.
     */
    public static String getRandomFoodName() {
        int index = RANDOM.nextInt(FOOD_NAMES.length);
        return FOOD_NAMES[index];
    }
}
