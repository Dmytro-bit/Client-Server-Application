package org.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {
    final int SERVER_PORT_NUMBER = 8888;

    public static void main(String[] args) {
        ClientSocket clientSocket = new ClientSocket();
        clientSocket.start();
    }

    public void start() {
        try (java.net.ServerSocket serverSocket = new java.net.ServerSocket(SERVER_PORT_NUMBER);) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     // connection is made.
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {
                    System.out.println("Client Message: A Server has connected.");
                }
            }
        } catch (IOException e) {
            System.out.println("Client Message: IOException: " + e);
        }
    }


}
