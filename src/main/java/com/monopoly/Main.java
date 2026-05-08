package com.monopoly;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.monopoly.ui.GameUI;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        GameUI gameUI = new GameUI(stage);
        Scene scene = new Scene(gameUI.getRoot(), 1024, 768);
        stage.setTitle("Monopoly - Design Patterns Demo");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}