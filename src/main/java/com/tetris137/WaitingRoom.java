package com.tetris137;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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

    // called by the FXML loader after the labels declared above are injected:
    public void initialize() {
        joinButton.setText("Join Game");
        usernameLabel.setText("Username: ");
        ipLabel.setText("Host IP Address: ");
        portLabel.setText("Port: ");

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

        // switch to the game scene
        Stage stage = (Stage) joinButton.getScene().getWindow();
        Tetris tetris = new Tetris();
        tetris.start(stage);
    }
}
