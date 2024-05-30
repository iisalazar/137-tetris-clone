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
                System.out.println("Client Thread: Received Message: " + message);
                message = message.replace("msg;", "");
                ud.addMessage(message + "\n");
                System.out.println("Client Thread: Added to messageBox.");
            } else if (message.contains("st4rting;")) {
                ud.setGameStarted();
            } else if (message.contains("query:getUsernames;")) {
                String playerNames = message.replace("query:getUsernames;", "");
                // names are separated by a comma
                String[] names = playerNames.split(",");
                for (String name : names) {
                    ud.setInitialPlayerState(name);
                }
            } else if (message.contains("event:gameStateUpdated")) {
                // Format
                // event:gameStateUpdated;<userName>;score:<score>
                String[] parts = message.split(";");
                String userName = parts[1];
                String[] scoreParts = parts[2].split(":");
                float score = Float.parseFloat(scoreParts[1]);
                ud.updateUserScore(userName, score);
            } else if (message.contains("UID;")) {
                message = message.replace("UID;", "");
                ud.setUID(message);
                System.out.println("User given UID: " + message);
            } else { // gamestate received
                if (message.contains("0;")) {
                    message = message.replace("0;", "");
                    ud.p0 = message;
                } else if (message.contains("1;")) {
                    message = message.replace("1;", "");
                    ud.p1 = message;
                } else if (message.contains("2;")) {
                    message = message.replace("2;", "");
                    ud.p2 = message;
                } else if (message.contains("3;")) {
                    message = message.replace("3;", "");
                    ud.p3 = message;
                }
            }
        }
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }
}
