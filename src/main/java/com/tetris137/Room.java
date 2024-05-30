package com.tetris137;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Room extends Application {
    public static void main (String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // first page should ask for user's name and host ip address with port
        // then, next page should show the tetris game
        // for now we will just show the tetris game
        // but we need to show the first room first
        FXMLLoader loader = new FXMLLoader(getClass().getResource("welcome-screen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();   
    }
    
    
}
