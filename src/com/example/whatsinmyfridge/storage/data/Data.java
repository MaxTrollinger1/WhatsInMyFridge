package com.example.whatsinmyfridge.storage.data;

import com.google.gson.annotations.Expose;

import java.util.Optional;

/**
 * Base class for all persisted data wrappers.
 * <p>
 * Holds common metadata (e.g., fileName and timestamp) for JSON files.
 */
public class Data {

    /**
     * Name of the JSON file (defaults to "data.json").
     */
    @Expose
    public String fileName = "data.json";

    /**
     * Timestamp recorded when the data was created or last updated.
     */
    @Expose
    public String time;

    public Data() { // Default constructor for Gson
    }

    /**
     * Constructs a Data wrapper with a timestamp.
     *
     * @param time timestamp string to record
     */
    public Data(String time)
    {
        this.time = time;
    }
}
