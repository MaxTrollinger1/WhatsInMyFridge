//import java.util.List;
package com.example.whatsinmyfridge.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.google.gson.annotations.Expose;

public class Ingredient {
    @Expose
    private String name;
    @Expose
    private double amount;
    @Expose
    private String unit;

    public Ingredient() {
    }

    public Ingredient(String name, double amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }

    public StringProperty amountProperty() {
        return new SimpleStringProperty(String.valueOf(amount));
    }

    public StringProperty unitProperty() {
        return new SimpleStringProperty(unit);
    }
}


