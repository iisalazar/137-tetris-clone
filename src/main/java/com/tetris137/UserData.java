package com.tetris137;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

// reference: https://youtu.be/MsgiJdf5njc?feature=shared

public class UserData {
    private static final UserData instance = new UserData();
    private String userName;
    private String ipGiven;
    private String ipServer;
    private InetAddress ipServerInet;
    private int portServer;
    private boolean isHost;
    private int port;
    private DatagramSocket sock;
    private ChatClientThread receiver;
    private int playerCount = 100;
    private boolean gameStarted = false;
    // private Chat chatBox;

    private static final TextArea messageArea = new TextArea();

    public static UserData getInstance() {
        messageArea.setMaxWidth(500);
        messageArea.setEditable(false);
        messageArea.setDisable(true);
        return instance;
    }

    public String getUserName() {
        return userName;
    }

    public String getIP() {
        return ipGiven;
    }

    public int getPort() {
        return port;
    }
    
    public String getServerIP() {
        return ipServer;
    }

    public int getServerPort() {
        return portServer;
    }

    public void setUsername(String s) {
        userName = s;
    }

    public void setIP(String ip) {
        ipGiven = ip;
    }

    public void setPort(int p) {
        port = p;
    }

    public void setServerIP(String ip) throws UnknownHostException {
        ipServer = ip;
        ipServerInet = InetAddress.getLocalHost();
    }

    public void setServerPort(int p) {
        portServer = p;
    }

    public void setIsHost(boolean b) {
        isHost = b;
    }

    public boolean getIsHost() {
        return isHost;
    }

    public void setSocket(DatagramSocket s) {
        sock = s;
    }

    public DatagramSocket getSocket() {
        return sock;
    }

    public void setClientThread (ChatClientThread t) {
        receiver = t;
    }

    public ChatClientThread getClientThread() {
        return receiver;
    }

    public InetAddress getInetAddress() {
        return ipServerInet;
    }

    public void setPlayerCount(int c) {
        playerCount = c;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public boolean getGameStarted() {
        return gameStarted;
    }

    public void setGameStarted() {
        gameStarted = true;
    }

    public TextArea getMessageArea() {
        return messageArea;
    }

    public void addMessage(String msg) {
        String current = messageArea.getText();
        messageArea.setText(current + msg);
    }

    // public void setChatBox(Chat cb) {
    //     chatBox = cb;
    // }

    // public Chat getChatBox() {
    //     return chatBox;
    // }
 }
