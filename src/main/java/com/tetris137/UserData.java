package com.tetris137;

// reference: https://youtu.be/MsgiJdf5njc?feature=shared

public class UserData {
    private static final UserData instance = new UserData();
    private String userName;
    private String ipGiven;
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

    public void setUsername(String s) {
        userName = s;
    }

    public void setIP(String ip) {
        ipGiven = ip;
    }

    public void setPort(int p) {
        port = p;
    }
}
