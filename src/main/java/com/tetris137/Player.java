package com.tetris137;

import java.net.InetAddress;

public class Player {
    private InetAddress address;
    private int port;
    private String username;

    public Player(InetAddress add, int p, String uname) {
        this.address = add;
        this.port = p;
        this.username = uname;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
