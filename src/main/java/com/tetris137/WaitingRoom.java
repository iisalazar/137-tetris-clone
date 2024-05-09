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
import java.net.*;

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
        // Store user data
        UserData ud = UserData.getInstance();
        
        // get the values from the text fields
        ud.setUsername(usernameField.getText());
        ud.setIP(ipField.getText());
        ud.setPort(Integer.parseInt(portField.getText()));
        // String username = usernameField.getText();
        // String ip = ipField.getText();
        // String port = portField.getText();

        // connect to the server
        // send the username, ip, and port
        // wait for the server to start the game
        System.out.println("Joining game with username: " + ud.getUserName() + " ip: " + ud.getIP() + " port: " + Integer.toString(ud.getPort()));

        // starts a server if the ip provided is same as ip of current
        if (ud.getIP().equals(InetAddress.getLocalHost().getHostAddress())) {
            // check if socket is used - hotfix for preventing conflicts in local network testing
            try {
                Socket portCheck = new Socket(ud.getIP(), ud.getPort());
                System.out.println("Socket already in use.");
            } catch (Exception e) {
            // } finally {
                ChatServer server = new ChatServer(ud.getIP(), ud.getPort());
                server.start();
                System.out.println("New Server Created at " + ud.getPort());
            }    
        }

        // switch to the game scene
        Stage stage = (Stage) joinButton.getScene().getWindow();
        // Tetris tetris = new Tetris();
        TetrisClient tetris = new TetrisClient();
        tetris.start(stage);
    }
}
