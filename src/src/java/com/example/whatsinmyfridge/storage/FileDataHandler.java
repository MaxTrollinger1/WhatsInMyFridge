package com.example.whatsinmyfridge.storage;

import java.io.*;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.whatsinmyfridge.storage.data.Data;

public class FileDataHandler {
    private String dataDirPath = "";

    private static final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Data.class, new DataAdapter()) // use data adapter
            .setPrettyPrinting()
            .create();

    public FileDataHandler(String dataDirPath) {
        this.dataDirPath = dataDirPath;
    }

    /// Reads all json files from directory
    public ArrayList<Data> load() {
        File directory = new File(dataDirPath);
        ArrayList<Data> loadedDataList = new ArrayList<>();

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));

            if (files != null) {
                for (File file : files) {
                    try {
                        StringBuilder dataToLoad = new StringBuilder();
                        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                dataToLoad.append(line);
                            }
                        }

                        String dataString = dataToLoad.toString();

                        Data loadedData = gson.fromJson(dataString, Data.class);

                        if (loadedData != null) {
                            loadedDataList.add(loadedData);
                        } else {
                            System.err.println("having trouble loading file");
                        }
                    } catch (Exception e) {
                        System.err.println("Error in load func: " + file.getName() + "\n" + e);
                    }
                }
            }
        } else {
            System.err.println("Wrong directory: " + dataDirPath);
        }

        return loadedDataList;
    }

    /// Writes data to specified file
    public void save(Data data) {
        String fullPath = dataDirPath + File.separator + data.fileName;
        File file = new File(fullPath);
        boolean result = file.getParentFile().mkdirs();

        try {
            String dataToStore = gson.toJson(data);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(dataToStore);
            }
        } catch (Exception e) {
            System.err.println("Error in save func: " + fullPath + "\n" + e);
        }
    }
}