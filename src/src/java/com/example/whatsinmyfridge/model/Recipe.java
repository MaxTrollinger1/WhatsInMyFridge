package com.example.whatsinmyfridge.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class Recipe {
    @Expose
    private String name;
    @Expose
    private String description;
    @Expose
    private List<Ingredient> ingredients;
    @Expose
    private String instructions;

    public Recipe()
    {}

    public Recipe(String name)
    {
        this.name = name;
        this.description = "";
        this.ingredients = new ArrayList<>();
        this.instructions = "";
    }


    public Recipe(String name, String description, List<Ingredient> ingredients, String instructions) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
