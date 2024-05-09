package com.tetris137;

// This sections is completed with https://youtu.be/7tznk2SWCZs?feature=shared as the main reference

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class ChatServer extends Thread {
    private static byte[] message = new byte[256];
    private int port = 8000; // use given port
    private InetAddress address;
    private String givenIp;
    private static DatagramSocket socket;
    private static ArrayList<Integer> players = new ArrayList<>();

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


        // address
        try {
            // address = InetAddress.getByName("localhost"); // todo: change later to ip of player1
            address = InetAddress.getByName(givenIp);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        System.out.println("PORT: " + port );

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

//     public void main(String[] args) {

//         // socket on port
//         try {
//             socket = new DatagramSocket(port); // todo: hange later to port of player1
//         } catch (SocketException j) {
//             throw new RuntimeException(j);
//         }


//         // address
//         try {
//             // address = InetAddress.getByName("localhost"); // todo: change later to ip of player1
//             address = InetAddress.getByName(givenIp);
//         } catch (UnknownHostException e) {
//             throw new RuntimeException(e);
//         }

//         System.out.println("PORT: " + port );

//         while (true) {

//             // receive incoming packets
//             DatagramPacket packet  = new DatagramPacket(message, message.length);
//             try {
//                 socket.receive(packet);
//             } catch (Exception e) {
//                 throw new RuntimeException(e);
//             }
//             //  convert packet data to string
//             String stringMessage  = new String(packet.getData(), 0, packet.getLength());
//             System.out.println("Server received: " + stringMessage);

//             // if a codeword is found, that means it is a new player and would be added to the players list.
//             if (stringMessage.contains("init;")) {
//                 players.add(packet.getPort());
//             }
//             // forward to clients/players
//             else {
//                 byte[] byteMessage = stringMessage.getBytes(); // string to bytes
//                 for (int other_port : players ) {
//                     DatagramPacket sendPacket = new DatagramPacket(byteMessage, byteMessage.length, address, other_port); // todo: add option for different ip
//                     try {
//                         socket.send(sendPacket);
//                     } catch (Exception e) {
//                         throw new RuntimeException(e);
//                     }
//                 }
                
//             }
//         }

//     }

//     public void setPort(int p) {
//         port = p;
//     }

//     public void setAdd(String add) {
//         givenIp = add;
//     }
// }
