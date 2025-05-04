package com.example.whatsinmyfridge;

import com.example.whatsinmyfridge.gui.RecipeListController;
import com.example.whatsinmyfridge.storage.DataPersistenceManager;
import com.example.whatsinmyfridge.storage.demo.DemoImplementor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WhatsInMyFridgeApp extends Application {

    private static Runnable onCloseCallback = null;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/whatsinmyfridgegui/Homescreen.fxml"));
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(
                getClass().getResource("/com/whatsinmyfridgegui/style.css").toExternalForm()
        );

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/whatsinmyfridgegui/RecipeList.fxml"));
        Parent recipeListRoot = loader.load();
        RecipeListController controller = loader.getController();

        DataPersistenceManager.initializeStaticInstance();
        DataPersistenceManager manager = DataPersistenceManager.instance;

        DemoImplementor useScript = new DemoImplementor();
        manager.RegisterPersistenceObject(controller);

        stage.setOnCloseRequest(event -> {
            System.out.println("Application is closing...");
            if (onCloseCallback != null) {
                onCloseCallback.run();
            }
        });

        Inventory inventory = new Inventory();

        manager.initialize();

        stage.setTitle("WhatsInMyFridge");
        stage.setScene(scene);
        stage.show();
    }

    public static void setOnCloseCallback(Runnable callback) {
        onCloseCallback = callback;
    }

    public static void main(String[] args) {

        setOnCloseCallback(() -> {
            System.out.println("Running onCloseCallback: Saving all data...");
            DataPersistenceManager.instance.SaveData();
        });

        launch(args);
    }
}
