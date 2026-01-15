package com.wiktoria.flow.flowsmapp;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Flow");
        DataManager.getInstance();
        primaryStage.setScene(new LoginView(primaryStage).getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}