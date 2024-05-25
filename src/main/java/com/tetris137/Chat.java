package com.tetris137;

// This sections is completed with https://youtu.be/7tznk2SWCZs?feature=shared as the main reference

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;

public class Chat extends Application{
    private static final TextField inputBox = new TextField();

    public void start(Stage primaryStage) {
        UserData ud = UserData.getInstance();

        inputBox.setMaxWidth(50);

        // inputBox.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> inputBox.requestFocus());

        inputBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String temp = "msg;" + ud.getUserName() + ": " + inputBox.getText(); // message to send// update messages on screen
                byte[] msg = temp.getBytes(); // convert to bytes
                inputBox.setText(""); // remove text from input box

                // create a packet & send
                DatagramPacket send = new DatagramPacket(msg, msg.length, ud.getInetAddress(), ud.getServerPort());
                try {
                    ud.getSocket().send(send);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // put everything on screen
        // chatBox.getChildren().addAll(ud.getMessageArea(), inputBox);
        // Scene scene = new Scene(chatBox);
        // primaryStage.setScene(scene);
        // primaryStage.show();
    }
}
