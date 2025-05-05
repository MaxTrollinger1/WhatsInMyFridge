package com.example.whatsinmyfridge;

import com.example.whatsinmyfridge.gui.HomescreenController;
import com.example.whatsinmyfridge.gui.RecipeListController;
import com.example.whatsinmyfridge.storage.DataPersistenceManager;
import com.example.whatsinmyfridge.storage.Inventory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class WhatsInMyFridgeApp extends Application {

    public static WhatsInMyFridgeApp instance;
    private static Runnable onCloseCallback = null;
    public static Inventory inventory;

    public HomescreenController homescreenController;
    public RecipeListController recipeListController;

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        FXMLLoader homescreenControllerLoader = new FXMLLoader(getClass().getResource("/com/whatsinmyfridgegui/Homescreen.fxml"));
        FXMLLoader recipeControllerLoader = new FXMLLoader(getClass().getResource("/com/whatsinmyfridgegui/RecipeList.fxml"));

        Pane pane = homescreenControllerLoader.load();
        Pane pane1 = recipeControllerLoader.load();

        homescreenController = (HomescreenController) homescreenControllerLoader.getController();
        recipeListController = (RecipeListController) recipeControllerLoader.getController();


        Scene scene = new Scene(pane, 900, 600);
        scene.getStylesheets().add(
                getClass().getResource("/com/whatsinmyfridgegui/style.css").toExternalForm()
        );

        DataPersistenceManager.initializeStaticInstance();
        DataPersistenceManager manager = DataPersistenceManager.instance;

        inventory = new Inventory();
        manager.RegisterPersistenceObject(inventory);

        stage.setOnCloseRequest(event -> {
            System.out.println("Application is closing...");
            if (onCloseCallback != null) {
                onCloseCallback.run();
            }
        });

        manager.initialize();

        stage.setTitle("WhatsInMyFridge");
        stage.setScene(scene);
        stage.show();
    }

    public static void setOnCloseCallback(Runnable callback) {
        onCloseCallback = callback;
    }

    public void onLoadedData()
    {
        if(homescreenController != null)
        {
            homescreenController.SetLists();
        }
        if(recipeListController != null)
        {
            recipeListController.SetLists();
        }
    }


    public static void main(String[] args) {

        setOnCloseCallback(() -> {
            System.out.println("Running onCloseCallback: Saving all data...");
            DataPersistenceManager.instance.SaveData();
        });

        launch(args);
    }
}
