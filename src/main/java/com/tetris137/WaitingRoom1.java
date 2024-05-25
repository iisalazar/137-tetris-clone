package com.tetris137;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Platform;

public class WaitingRoom1 extends Application {
    
    private static final Label title = new Label("TETRIS");
    private Label playerCountLabel = new Label("3");
    private Label userType = new Label("You are a ...");
    private int playerCount = 3;
    private VBox root = new VBox();
    private Button startGame = new Button("Start Game");
    private Label serverCred = new Label();
    private Timeline t;
    
    @Override
    public void start(Stage stage) throws InterruptedException {
        UserData ud = UserData.getInstance();

        serverCred.setText("Server Credentials: " + ud.getServerIP() + " | " + ud.getServerPort());
  
        byte[] msgb = ("ReqP;").getBytes();
        DatagramPacket requestPlayerCount = new DatagramPacket(msgb, msgb.length, ud.getInetAddress(), ud.getServerPort());
        try {
            ud.getSocket().send(requestPlayerCount);
            System.out.println("Client: Player Count Request Sent.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (ud.getIsHost()) {
            userType.setText("You are a Host!");
            root.getChildren().addAll(title, serverCred, userType, playerCountLabel, startGame);
        } else {
            userType.setText("Waiting for Host to start the Game!");
            root.getChildren().addAll(title, serverCred, userType, playerCountLabel);
        }

        // Thread.sleep(2000);
        t = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            playerCountLabel.setText("Number of Players: " + ud.getPlayerCount());

            if (ud.getGameStarted()) {

                playerCountLabel.setText("GAME STARTED");
            //     TetrisMultiplayer mp = new TetrisMultiplayer();
            //     mp.start(stage);
                t.stop();
                Chat c = new Chat();
                TetrisClient tetris = new TetrisClient();
                try {
                    tetris.start(stage);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        }));
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();

        startGame.setOnAction(event -> {
            byte[] startmsg = ("st4rt;").getBytes();
            DatagramPacket start = new DatagramPacket(startmsg, startmsg.length, ud.getInetAddress(), ud.getServerPort());
            try {
                ud.getSocket().send(start);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Host started the game.");
        });

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
    
    
}
