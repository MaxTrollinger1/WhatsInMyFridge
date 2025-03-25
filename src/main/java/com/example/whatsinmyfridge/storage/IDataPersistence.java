package com.example.whatsinmyfridge.storage;

import com.example.whatsinmyfridge.storage.data.*;

public interface IDataPersistence {
    void loadData(Data data);

    void saveData(Data data);
}