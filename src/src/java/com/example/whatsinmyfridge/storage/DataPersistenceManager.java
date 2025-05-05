package com.example.whatsinmyfridge.storage;

import com.example.whatsinmyfridge.Main;
import com.example.whatsinmyfridge.storage.data.*;

import java.io.File;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

public class DataPersistenceManager {
    private String dirPath = "WhatsInMyFridge";

    private ArrayList<Data> wrappers = new ArrayList<>();

    private List<IDataPersistence> dataPersistenceObjects = new ArrayList<>();
    private FileDataHandler dataHandler;


    //region Initialization

    public static DataPersistenceManager instance;


    public DataPersistenceManager() {
        // Subscribe to close event (save data on application exit)
        Main.setOnCloseCallback(this::SaveData);
    }

    /// Initializes the data handler
    public void initialize() {
        this.dataHandler = new FileDataHandler(getAppDataLocation() + File.separator + dirPath);
        LoadData(); // Attempt to load data
    }

    //endregion

    //region Operations

    /// If no save could be found, NewSave is called to initialize the individual wrapper files
    public void NewSave()
    {
        wrappers.clear();

        wrappers.add(new PantryWrapper(LocalTime.now().toString()));
        wrappers.add(new RecipeWrapper(LocalTime.now().toString()));

        SaveData();
    }

    /// Loads saved data, or initializes new data if none is found
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

    /// Saves the current state
    public void SaveData() {
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

    /// Registers a new data object
    public void RegisterPersistenceObject(IDataPersistence persistenceObject)
    {
        dataPersistenceObjects.add(persistenceObject);
    }

    //endregion

    //region Utility

    /// Initializes the static instance of DataPersistenceManager
    public static void initializeStaticInstance() {
        if (instance == null) { // Singleton
            instance = new DataPersistenceManager();
        }
    }

    /// Returns the path for application data storage location
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
