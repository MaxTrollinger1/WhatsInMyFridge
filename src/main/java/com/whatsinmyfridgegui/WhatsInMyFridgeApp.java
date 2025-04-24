package com.example.whatsinmyfridgegui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WhatsInMyFridgeApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Homescreen.fxml"));
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(
                getClass().getResource("style.css").toExternalForm()
        );
        stage.setTitle("WhatsInMyFridge");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
