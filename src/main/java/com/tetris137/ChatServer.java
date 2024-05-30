package com.tetris137;

// This sections is completed with https://youtu.be/7tznk2SWCZs?feature=shared as the main reference

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class ChatServer extends Thread {
    private static byte[] message = new byte[256];
    private int port; // use given port
    private String givenIp;
    private static DatagramSocket socket;
    private ArrayList<Player> players = new ArrayList<>();

    public ChatServer(String ipNew, int portNew) {
        this.port = portNew;
        this.givenIp = ipNew;
    }

    @Override
    public void run() {
        // socket on port
        try {
            socket = new DatagramSocket(port); // todo: hange later to port of player1
        } catch (SocketException j) {
            throw new RuntimeException(j);
        }

        System.out.println("SERVER PORT: " + port);

        while (true) {
            // receive incoming packets
            DatagramPacket packet = new DatagramPacket(message, message.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // convert packet data to string
            String stringMessage = new String(packet.getData(), 0, packet.getLength());
            // System.out.println("Server received: " + stringMessage);

            // if a codeword is found, that means it is a new player and would be added to
            // the players list.
            if (stringMessage.contains("init;")) {
                byte[] returnUID = ("UID;" + String.valueOf(players.size())).getBytes();

                String uname = stringMessage.replace("init;", "");
                Player newP = new Player(packet.getAddress(), packet.getPort(), uname);
                players.add(newP);
                System.out.println("Server: Player Added + " + uname);

                DatagramPacket sendPacket = new DatagramPacket(returnUID, returnUID.length, packet.getAddress(), packet.getPort());
                try {
                    socket.send(sendPacket);
                    System.out.println("Server: Assigned " + uname + " to " + (players.size()-1));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            } else if (stringMessage.contains("ReqP;")) {
                System.out.println("Server: Received Player Count Request.");
                byte[] byteMessage = ("ReqP;" + String.valueOf(players.size())).getBytes();
                for (Player player : players) {
                    DatagramPacket sendPacket = new DatagramPacket(byteMessage, byteMessage.length, player.getAddress(),
                            player.getPort()); // todo: add option for different ip
                    try {
                        socket.send(sendPacket);
                        System.out.println("Server: Sent Player Count.");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } 
            else if (stringMessage.contains("msg;")) {
                // stringMessage = stringMessage.replace("msg;", "");
                byte[] byteMessage = stringMessage.getBytes(); // string to bytes
                for (Player player : players) {
                    DatagramPacket sendPacket = new DatagramPacket(byteMessage, byteMessage.length, player.getAddress(),
                            player.getPort()); // todo: add option for different ip
                    try {
                        socket.send(sendPacket);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

            } else if (stringMessage.contains("st4rt;")) {
                byte[] byteMessage = ("st4rting;").getBytes();
                for (Player player : players) {
                    DatagramPacket sendPacket = new DatagramPacket(byteMessage, byteMessage.length, player.getAddress(),
                            player.getPort());
                    try {
                        socket.send(sendPacket);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                // eventNames 
                // event:gameStateUpdated;<userName>;score:<score>;lines:<lines>
            } else if(stringMessage.contains("event:gameStateUpdated;")) {
                byte[] byteMessage = stringMessage.getBytes(); // string to bytes
                for (Player player : players) {
                    DatagramPacket sendPacket = new DatagramPacket(byteMessage, byteMessage.length, player.getAddress(),
                            player.getPort());
                    try {
                        socket.send(sendPacket);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            else if(stringMessage.contains("query:getUsernames")) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Player player : players) {
                    stringBuilder.append(player.getUsername()).append(",");
                }
                byte[] byteMessage = ("query:getUsernames;" + stringBuilder.toString()).getBytes();
                for (Player player : players) {
                    DatagramPacket sendPacket = new DatagramPacket(byteMessage, byteMessage.length, player.getAddress(),
                            player.getPort());
                    try {
                        socket.send(sendPacket);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } else { // gamestate received and should be forwarded;
                for (Player player : players) {
                    DatagramPacket forwardGS = new DatagramPacket(packet.getData(), packet.getData().length, player.getAddress(), player.getPort());
                    try {
                        socket.send(forwardGS);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }            
            }
        }
    }
}