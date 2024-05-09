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
    private DatagramSocket socket;
    private InetAddress address;
    private String givenIp;
    private String username = "Ernest"; // todo: use given username
    private int server_port = 8000; // todo: use given port

    // gui
    private static final TextArea messageArea = new TextArea();
    private static final TextField inputBox = new TextField();

    public void main(String[] args) {

        System.out.println("Creating chat client for " + username);

        try {
            socket = new DatagramSocket(); // would use available port
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        try {
            address  = InetAddress.getByName(givenIp); // todo: make changeable
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // receiving messages
        ChatClientThread clientThread = new ChatClientThread(socket, messageArea);
        clientThread.start();

        // sent initilization to server
        byte[] uuid = ("init;" + username).getBytes();
        DatagramPacket initialize = new DatagramPacket(uuid, uuid.length, address, server_port);
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
                DatagramPacket send = new DatagramPacket(msg, msg.length, address, server_port);
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

    public void setUsername(String uname) {
        username = uname;
    }

    public void setAdd(String add) {
        givenIp = add;
    }

    public void setPort(int p) {
        server_port = p;
    }


}
