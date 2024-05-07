package com.tetris137;

// This sections is completed with https://youtu.be/7tznk2SWCZs?feature=shared as the main reference

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class ChatServer {
    private static byte[] message = new byte[256];
    private static final int PORT = 8000; // use given port
    private static DatagramSocket socket;
    private static ArrayList<Integer> players = new ArrayList<>();
    private static final InetAddress address;
    
    // socket on port
    static {
        try {
            socket = new DatagramSocket(PORT); // todo: hange later to port of player1
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    // address
    static {
        try {
            address = InetAddress.getByName("localhost"); // todo: change later to ip of player1
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        System.out.println("PORT: " + PORT );

        while (true) {

            // receive incoming packets
            DatagramPacket packet  = new DatagramPacket(message, message.length);
            try {
                socket.receive(packet);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //  convert packet data to string
            String stringMessage  = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Server received: " + stringMessage);

            // if a codeword is found, that means it is a new player and would be added to the players list.
            if (stringMessage.contains("init;")) {
                players.add(packet.getPort());
            }
            // forward to clients/players
            else {
                byte[] byteMessage = stringMessage.getBytes(); // string to bytes
                for (int other_port : players ) {
                    DatagramPacket sendPacket = new DatagramPacket(byteMessage, byteMessage.length, address, other_port); // todo: add option for different ip
                    try {
                        socket.send(sendPacket);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                
            }
        }

    }
}
