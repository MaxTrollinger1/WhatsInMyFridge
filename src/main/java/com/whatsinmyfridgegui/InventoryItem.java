package com.example.whatsinmyfridgegui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InventoryItem {
    private final StringProperty name   = new SimpleStringProperty();
    private final StringProperty amount = new SimpleStringProperty();

    public InventoryItem(String name, String amount) {
        this.name.set(name);
        this.amount.set(amount);
    }

    // existing properties/getters
    public StringProperty nameProperty()   { return name;   }
    public StringProperty amountProperty() { return amount; }
    public String getName()   { return name.get();   }
    public String getAmount() { return amount.get(); }

    // **NEW** setter for name (needed by your inline‐edit)
    public void setName(String newName) {
        name.set(newName);
    }

    // you already have this one
    public void setAmount(String amt) {
        amount.set(amt);
    }
}
