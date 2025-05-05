package com.example.whatsinmyfridge.gui;

import com.example.whatsinmyfridge.model.Ingredient;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InventoryItem {
    private final StringProperty name = new SimpleStringProperty(this, "name");
    private final StringProperty amount = new SimpleStringProperty(this, "amount");
    private final StringProperty measurement = new SimpleStringProperty(this, "measurement");

    public InventoryItem(String name, String amount) {
        this(name, amount, "");
    }

    public InventoryItem(String name, String amount, String measurement) {
        this.name.set(name);
        this.amount.set(amount);
        this.measurement.set(measurement);
    }

    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public StringProperty nameProperty() {
        return name;
    }

    public String getAmount() {
        return amount.get();
    }
    public void setAmount(String amount) {
        this.amount.set(amount);
    }
    public StringProperty amountProperty() {
        return amount;
    }

    public String getMeasurement() {
        return measurement.get();
    }
    public void setMeasurement(String measurement) {
        this.measurement.set(measurement);
    }
    public StringProperty measurementProperty() {
        return measurement;
    }
}
