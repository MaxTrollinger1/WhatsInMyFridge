package com.example.whatsinmyfridge.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

/**
 * Represents a recipe containing a name, description, ingredients list, and cooking instructions.
 * Supports JSON serialization for saving and loading.
 */
public class Recipe {
    @Expose
    private String name;
    @Expose
    private String description;
    @Expose
    private List<Ingredient> ingredients;
    @Expose
    private String instructions;

    /**
     * Default constructor for deserialization.
     */
    public Recipe() {}

    /**
     * Constructs a Recipe with only a name. Description, ingredients, and instructions will be empty.
     *
     * @param name the name of the recipe
     */
    public Recipe(String name) {
        this.name = name;
        this.description = "";
        this.ingredients = new ArrayList<>();
        this.instructions = "";
    }

    /**
     * Constructs a Recipe with full details.
     *
     * @param name         the name of the recipe
     * @param description  a short description of the recipe
     * @param ingredients  a list of ingredients required
     * @param instructions detailed cooking instructions
     */
    public Recipe(String name, String description, List<Ingredient> ingredients, String instructions) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    /**
     * Gets the name of the recipe.
     *
     * @return the recipe's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the recipe.
     *
     * @param name the new recipe name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the recipe.
     *
     * @return the recipe description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the recipe.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the list of ingredients required for the recipe.
     *
     * @return the list of ingredients
     */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Sets the list of ingredients for the recipe.
     *
     * @param ingredients the new list of ingredients
     */
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Gets the cooking instructions for the recipe.
     *
     * @return the instructions
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Sets the cooking instructions for the recipe.
     *
     * @param instructions the new instructions
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
