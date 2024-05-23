package com.tetris137;

import java.net.InetAddress;

public class Player {
    private InetAddress address;
    private int port;

    public Player(InetAddress add, int p) {
        this.address = add;
        this.port = p;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
