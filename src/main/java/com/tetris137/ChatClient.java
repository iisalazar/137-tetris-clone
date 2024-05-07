package com.tetris137;

// This sections is completed with https://youtu.be/7tznk2SWCZs?feature=shared as the main reference

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;

public class ChatClient extends Application{
    private static final DatagramSocket socket;
    private static final InetAddress address;
    private static final String username = "Ernest"; // todo: use given username
    private static final int SERVER_PORT = 8000; // todo: use given port

    // gui
    private static final TextArea messageArea = new TextArea();
    private static final TextField inputBox = new TextField();

    static {
        try {
            socket = new DatagramSocket(); // would use available port
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        try {
            address  = InetAddress.getByName("localhost"); // todo: make changeable
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        // receiving messages
        ChatClientThread clientThread = new ChatClientThread(socket, messageArea);
        clientThread.start();

        // sent initilization to server
        byte[] uuid = ("init;" + username).getBytes();
        DatagramPacket initialize = new DatagramPacket(uuid, uuid.length, address, SERVER_PORT);
        try {
            socket.send(initialize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        

        launch();
    }

    public void start(Stage primaryStage) {
        messageArea.setMaxWidth(500);
        messageArea.setEditable(false);


        inputBox.setMaxWidth(500);
        inputBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String temp = username + ";" + inputBox.getText(); // message to send
                messageArea.setText(messageArea.getText() + inputBox.getText() + "\n"); // update messages on screen
                byte[] msg = temp.getBytes(); // convert to bytes
                inputBox.setText(""); // remove text from input box

                // create a packet & send
                DatagramPacket send = new DatagramPacket(msg, msg.length, address, SERVER_PORT);
                try {
                    socket.send(send);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // put everything on screen
        Scene scene = new Scene(new VBox(35, messageArea, inputBox), 550, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    


}
