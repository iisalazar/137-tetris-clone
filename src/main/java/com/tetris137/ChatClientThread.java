package com.tetris137;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ChatClientThread extends Thread {

    private DatagramSocket socket;
    private byte[] incoming = new byte[256];

    private TextArea textArea;

    public ChatClientThread(DatagramSocket socket, TextArea textArea) {
        this.socket = socket;
        this.textArea = textArea;
    }

    public ChatClientThread(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("starting thread");
        UserData ud = UserData.getInstance();

        while (true) {
            DatagramPacket packet = new DatagramPacket(incoming, incoming.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String message = new String(packet.getData(), 0, packet.getLength());

            if (message.contains("ReqP;")) {
                message = message.replace("ReqP;", "");
                ud.setPlayerCount(Integer.parseInt(message));
                System.out.println("Client Thread: Received number of players " + message);
            } else if (message.contains("msg;")) {
                message = message.replace("msg;", "");
                continue;
                // ud.setMessageArea(message + "\n");
            } else if (message.contains("st4rting;")) {
                ud.setGameStarted();
            }
            // String current = textArea.getText();
            // textArea.setText(current + message);
        }
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }
}
