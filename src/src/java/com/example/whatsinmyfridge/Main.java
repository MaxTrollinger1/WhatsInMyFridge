package com.example.whatsinmyfridge;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Runnable onCloseCallback = null;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            System.out.println("Application is closing...");
            onCloseCallback.run();
        });

        stage.show();
    }

    public static void setOnCloseCallback(Runnable callback) {
        onCloseCallback = callback;
    }

    public static void main(String[] args) {
        launch();
    }
}