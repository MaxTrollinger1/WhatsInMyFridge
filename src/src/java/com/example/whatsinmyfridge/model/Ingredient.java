package com.example.whatsinmyfridge.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.google.gson.annotations.Expose;

/**
 * Represents an ingredient with a name, amount, and unit of measurement.
 * Supports JavaFX property bindings and JSON serialization.
 */
public class Ingredient {
    @Expose
    private String name;
    @Expose
    private double amount;
    @Expose
    private String unit;

    /**
     * Default constructor for deserialization.
     */
    public Ingredient() {}

    /**
     * Constructs an Ingredient with specified name, amount, and unit.
     *
     * @param name   the name of the ingredient
     * @param amount the quantity of the ingredient
     * @param unit   the unit of measurement for the ingredient
     */
    public Ingredient(String name, double amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    /**
     * Gets the name of the ingredient.
     *
     * @return the ingredient's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the ingredient.
     *
     * @param name the new name of the ingredient
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the amount of the ingredient.
     *
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the ingredient.
     *
     * @param amount the new amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Gets the unit of measurement for the ingredient.
     *
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the unit of measurement for the ingredient.
     *
     * @param unit the new unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Returns a JavaFX StringProperty representing the ingredient's name.
     *
     * @return the name property
     */
    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }

    /**
     * Returns a JavaFX StringProperty representing the ingredient's amount.
     *
     * @return the amount property as a String
     */
    public StringProperty amountProperty() {
        return new SimpleStringProperty(String.valueOf(amount));
    }

    /**
     * Returns a JavaFX StringProperty representing the ingredient's unit.
     *
     * @return the unit property
     */
    public StringProperty unitProperty() {
        return new SimpleStringProperty(unit);
    }
}
