package com.tetris137;

import java.net.DatagramPacket;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;

public class WaitingRoom1 extends Application {
    
    private static final Label title = new Label("TETRIS");
    private Label playerCountLabel = new Label("3");
    private Label userType = new Label("You are the host");
    private int playerCount = 3;
    VBox root = new VBox();
    
    @Override
    public void start(Stage stage) throws InterruptedException {
        UserData ud = UserData.getInstance();
  
        byte[] msgb = ("ReqP;").getBytes();
        DatagramPacket requestPlayerCount = new DatagramPacket(msgb, msgb.length, ud.getInetAddress(), ud.getServerPort());
        try {
            ud.getSocket().send(requestPlayerCount);
            System.out.println("Client: Player Count Request Sent.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Thread.sleep(2000);        
        Timer upd = new Timer();
        TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                    playerCountLabel.setText("Number of Players: " + ud.getPlayerCount());
                }
            });}
        };
        upd.schedule(updateTask, 300);

        root.getChildren().addAll(title, userType, playerCountLabel);
        Scene scene = new Scene(root, 200, 200);
        stage.setScene(scene);
        stage.show();
    }
    
    
}
