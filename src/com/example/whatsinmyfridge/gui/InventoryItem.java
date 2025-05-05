package com.example.whatsinmyfridge.gui;

import com.example.whatsinmyfridge.model.Ingredient;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Wraps an Ingredient for display in JavaFX tables, with observable string properties.
 */
public class InventoryItem {
    private final StringProperty name = new SimpleStringProperty(this, "name");
    private final StringProperty amount = new SimpleStringProperty(this, "amount");
    private final StringProperty measurement = new SimpleStringProperty(this, "measurement");

    /**
     * Constructs an InventoryItem with all fields.
     * @param name item name
     * @param amount quantity as string
     * @param measurement unit or descriptor
     */
    public InventoryItem(String name, String amount, String measurement) {
        this.name.set(name);
        this.amount.set(amount);
        this.measurement.set(measurement);
    }

    /** @return name property value */
    public String getName() {
        return name.get();
    }
    /** @param name new name */
    public void setName(String name) {
        this.name.set(name);
    }
    /** @return observable name property */
    public StringProperty nameProperty() {
        return name;
    }

    /** @return amount property value */
    public String getAmount() {
        return amount.get();
    }
    /** @param amount new amount */
    public void setAmount(String amount) {
        this.amount.set(amount);
    }
    /** @return observable amount property */
    public StringProperty amountProperty() {
        return amount;
    }

    /** @return measurement property value */
    public String getMeasurement() {
        return measurement.get();
    }
    /** @param measurement new measurement */
    public void setMeasurement(String measurement) {
        this.measurement.set(measurement);
    }
    /** @return observable measurement property */
    public StringProperty measurementProperty() {
        return measurement;
    }
}
