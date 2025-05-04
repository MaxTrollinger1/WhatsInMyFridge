package com.example.whatsinmyfridge.storage.data;

import com.google.gson.annotations.Expose;

public class FoodItem {

    @Expose
    private String name;

    @Expose
    private int quantity;

    public FoodItem() { // Default constructor
    }

    public FoodItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    //region Mutable

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //endregion

    //region Utility

    @Override
    public String toString() {
        return "FoodItem{name='" + name + "'}";
    }

    //endregion


}
