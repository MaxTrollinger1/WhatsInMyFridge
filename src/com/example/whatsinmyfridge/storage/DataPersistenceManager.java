package com.example.whatsinmyfridge.storage;

import com.example.whatsinmyfridge.storage.data.*;

import java.io.File;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Manages loading and saving of application data wrappers via FileDataHandler.
 * Coordinates IDataPersistence objects and ensures data is initialized or created as needed.
 */
public class DataPersistenceManager {
    /** Singleton instance. */
    public static DataPersistenceManager instance;

    private String dirPath = "WhatsInMyFridge";

    private ArrayList<Data> wrappers = new ArrayList<>();

    private List<IDataPersistence> dataPersistenceObjects = new ArrayList<>();
    private FileDataHandler dataHandler;


    //region Initialization

    /**
     * Constructs the manager (must call initializeStaticInstance before use).
     */
    public DataPersistenceManager() {

    }

    /**
     * Sets up FileDataHandler and loads existing data or creates new save files.
     */
    public void initialize() {
        this.dataHandler = new FileDataHandler(getAppDataLocation() + File.separator + dirPath);
        LoadData(); // Attempt to load data
    }

    //endregion

    //region Operations

    /**
     * Generates fresh data wrappers and persists them to disk.
     */
    public void NewSave()
    {
        wrappers.clear();

        wrappers.add(new PantryWrapper(LocalTime.now().toString()));
        wrappers.add(new RecipeWrapper(LocalTime.now().toString()));

        SaveData();
    }

    /**
     * Loads JSON data into wrappers and dispatches to registered persistence objects.
     * Creates new save if no existing data found.
     */
    public void LoadData() {
        this.wrappers.clear();
        this.wrappers = dataHandler.load();

        if(wrappers.isEmpty())
        {
            System.out.println("No saved data found. Creating new save...");
            NewSave();
            return;
        }

        // Pass loaded data to all persistence objects
        for (IDataPersistence dataPersistenceObj : dataPersistenceObjects) {
            for (Data data : wrappers)
            {
                dataPersistenceObj.loadData(data);
            }
        }
    }

    /**
     * Instructs persistence objects to write state into wrappers, then saves each wrapper to disk.
     */
    public void SaveData() {
        // iterate through persistence objects and request save
        for (IDataPersistence dataPersistenceObj : dataPersistenceObjects) {
            for (Data data : wrappers)
            {
                dataPersistenceObj.saveData(data);
            }
        }

        for (Data data : wrappers)
        {
            dataHandler.save(data);
        }
    }

    /**
     * Registers an object that should participate in data load/save cycles.
     * @param persistenceObject object implementing IDataPersistence
     */
    public void RegisterPersistenceObject(IDataPersistence persistenceObject)
    {
        dataPersistenceObjects.add(persistenceObject);
    }

    //endregion

    //region Utility

    /**
     * Ensures singleton instance exists. Call before using the manager.
     */
    public static void initializeStaticInstance() {
        if (instance == null) { // Singleton
            instance = new DataPersistenceManager();
        }
    }

    /**
     * Determines OS-appropriate user data folder (e.g., %LOCALAPPDATA% on Windows).
     * @return absolute path to user-specific app data location
     */
    public String getAppDataLocation() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            String localAppData = System.getenv("LOCALAPPDATA");
            return localAppData != null ? localAppData : System.getenv("APPDATA");
        } else if (os.contains("mac") || os.contains("nix") || os.contains("nux")) {
            return System.getProperty("user.home") + "/Library/Application Support";
        } else {
            return System.getProperty("user.home") + "/.config";
        }
    }

    //endregion
}
