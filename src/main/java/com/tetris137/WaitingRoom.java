package com.tetris137;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;


import java.net.*;

public class WaitingRoom {
    // welcome-screen.fxml
    @FXML
    TextField usernameField;
    @FXML
    TextField portHostField;
    @FXML
    TextField ipJoinField;
    @FXML
    TextField portJoinField;
    @FXML
    Button hostButton;
    @FXML
    Button joinButton;

    // waiting-screen.fxml
    @FXML 
    Button startButton;

    private String userIP;

    // called by the FXML loader after the labels declared above are injected:
    public void initialize() throws UnknownHostException {
        try {
            userIP = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(InetAddress.getLocalHost().getHostName());
    }

    public void joinGame(ActionEvent event) throws Exception {
        // Store user data
        UserData ud = UserData.getInstance();
        
        // get the values from the text fields
        ud.setUsername(usernameField.getText());
        ud.setIP(userIP);
        ud.setServerIP(ipJoinField.getText());
        ud.setServerPort(Integer.parseInt(portHostField.getText()));
        ud.setIsHost(false);

        // // starts a server if the ip provided is same as ip of current
        // if (ud.getIP().equals(InetAddress.getLocalHost().getHostAddress())) {
        //     // check if socket is used - hotfix for preventing conflicts in local network testing
        //     try {
        //         Socket portCheck = new Socket(ud.getIP(), ud.getPort());
        //         System.out.println("Socket already in use.");
        //     } catch (Exception e) {
        //         ChatServer server = new ChatServer(ud.getIP(), ud.getPort());
        //         server.start();
        //         System.out.println("New Server Created at " + ud.getPort());
        //     }    
        // }

        // // switch to the game scene
        // Stage stage = (Stage) joinButton.getScene().getWindow();
        // // Tetris tetris = new Tetris();
        // TetrisClient tetris = new TetrisClient();
        // tetris.start(stage);

        nextRoom(event);

        
    }

    public void hostGame(ActionEvent event) throws Exception {
        // Store user data
        UserData ud = UserData.getInstance();

        // set player details
        ud.setUsername(usernameField.getText());
        ud.setIP(userIP);
        ud.setPort(Integer.parseInt(portHostField.getText()));
        ud.setServerIP(userIP);
        ud.setServerPort(Integer.parseInt(portHostField.getText()));
        ud.setIsHost(true);

        ChatServer server = new ChatServer(ud.getIP(), ud.getPort());
        server.start();

        nextRoom(event);

        // todo: add screen
    }

    public void nextRoom(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("waiting-screen.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
