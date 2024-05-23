package com.tetris137;

// reference: https://youtu.be/MsgiJdf5njc?feature=shared

public class UserData {
    private static final UserData instance = new UserData();
    private String userName;
    private String ipGiven;
    private String ipServer;
    private int portServer;
    private boolean isHost;
    private int port;

    public static UserData getInstance() {
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

    public void setServerIP(String ip) {
        ipServer = ip;
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
}
