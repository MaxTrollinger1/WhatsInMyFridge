package com.example.whatsinmyfridge.storage.data;

import com.google.gson.annotations.Expose;

import java.util.Optional;

///  Base class for any saved data
public class Data {

    @Expose
    public String fileName = "data.json";

    @Expose
    public String time;

    public Data() { // Default constructor for Gson
    }

    public Data(String time)
    {
        this.time = time;
    }
}
