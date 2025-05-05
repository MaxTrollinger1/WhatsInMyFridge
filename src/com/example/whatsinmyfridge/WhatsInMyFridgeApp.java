package com.example.whatsinmyfridge;

import com.example.whatsinmyfridge.gui.HomescreenController;
import com.example.whatsinmyfridge.gui.RecipeListController;
import com.example.whatsinmyfridge.storage.DataPersistenceManager;
import com.example.whatsinmyfridge.storage.Inventory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Main application class for WhatsInMyFridge.
 * <p>
 * Initializes the JavaFX UI, configures the DataPersistenceManager for automatic loading/saving,
 * and holds the central Inventory, which maintains lists of food items and recipes.
 */
public class WhatsInMyFridgeApp extends Application {

    /**
     * Singleton instance of the application, set on start().
     */
    public static WhatsInMyFridgeApp instance;

    /**
     * Callback executed when the application closes (e.g., to trigger a save).
     */
    private static Runnable onCloseCallback = null;

    /**
     * Central inventory: stores and manages lists of food items and recipes.
     * Its state is persisted by DataPersistenceManager.
     */
    public static Inventory inventory;

    public HomescreenController homescreenController;
    public RecipeListController recipeListController;


    /**
     * JavaFX entry point. Sets up UI, initializes persistence, and registers the inventory
     * so loaded data populates the inventory lists and any changes are saved automatically.
     *
     * @param stage primary stage provided by JavaFX
     * @throws Exception if FXML resources fail to load
     */
    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        // Load needed controllers from fxmls
        FXMLLoader homescreenControllerLoader = new FXMLLoader(getClass().getResource("/com/whatsinmyfridgegui/Homescreen.fxml"));
        FXMLLoader recipeControllerLoader = new FXMLLoader(getClass().getResource("/com/whatsinmyfridgegui/RecipeList.fxml"));

        Pane pane = homescreenControllerLoader.load();
        Pane pane1 = recipeControllerLoader.load();

        homescreenController = (HomescreenController) homescreenControllerLoader.getController();
        recipeListController = (RecipeListController) recipeControllerLoader.getController();

        // Set Icon
        Image icon = new Image(getClass().getResourceAsStream("/icons/appicon.png"));
        stage.getIcons().add(icon);

        // Setup scene
        Scene scene = new Scene(pane, 900, 600);
        scene.getStylesheets().add(
                getClass().getResource("/com/whatsinmyfridgegui/style.css").toExternalForm()
        );

        // Settings
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.centerOnScreen();

        // Prepare persistence: DataPersistenceManager handles loading/saving of all registered objects
        DataPersistenceManager.initializeStaticInstance();
        DataPersistenceManager manager = DataPersistenceManager.instance;

        // Inventory holds food & recipe lists; register it so its data is loaded and saved
        inventory = new Inventory();
        manager.RegisterPersistenceObject(inventory);

        // Handle window close to save data
        stage.setOnCloseRequest(event -> {
            System.out.println("Application is closing...");
            if (onCloseCallback != null) {
                onCloseCallback.run();
            }
        });

        manager.initialize();

        // Display the window
        stage.setTitle("What's In My Fridge");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Registers a callback to execute when the application window closes.
     * @param callback runnable to run on close
     */
    public static void setOnCloseCallback(Runnable callback) {
        onCloseCallback = callback;
    }

    /**
     * Notifies controllers to update their display lists after data is loaded.
     */
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

    /**
     * Application entry point: sets up save-on-exit behavior and launches JavaFX.
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        setOnCloseCallback(() -> {
            System.out.println("Running onCloseCallback: Saving all data...");
            DataPersistenceManager.instance.SaveData();
        });

        launch(args);
    }
}
