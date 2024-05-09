package com.tetris137;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class WaitingRoom {
    @FXML
    Label usernameLabel;
    @FXML
    Label ipLabel;
    @FXML
    Label portLabel;
    @FXML
    TextField usernameField;
    @FXML
    TextField ipField;
    @FXML
    TextField portField;
    @FXML
    Button joinButton;

    // waiting-room-1.fxml modification
    @FXML
    Label userIpLabel;
    private String userIP;

    // called by the FXML loader after the labels declared above are injected:
    public void initialize() {
        // joinButton.setText("Join Game");
        // usernameLabel.setText("Username: ");
        // ipLabel.setText("Host IP Address: ");
        // portLabel.setText("Port: ");

        // waiting-room-1.fxml modification
        try {
            userIP = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        userIpLabel.setText(userIP);

        // set the prompt text for the text fields
        usernameField.setPromptText("Enter your username");
        ipField.setPromptText("Enter the host IP address");
        portField.setPromptText("Enter the port number");
    }

    public void joinGame() throws Exception {
        // get the values from the text fields
        String username = usernameField.getText();
        String ip = ipField.getText();
        String port = portField.getText();

        // connect to the server
        // send the username, ip, and port
        // wait for the server to start the game
        System.out.println("Joining game with username: " + username + " ip: " + ip + " port: " + port);

        // starts a server 
        ChatServer server = new ChatServer(ip, Integer.parseInt(port));
        server.start();

        // switch to the game scene
        Stage stage = (Stage) joinButton.getScene().getWindow();
        // Tetris tetris = new Tetris();
        TetrisClient tetris = new TetrisClient();
        tetris.start(stage);
    }
}
