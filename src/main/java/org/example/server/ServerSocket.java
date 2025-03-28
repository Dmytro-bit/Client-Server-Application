package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSocket {
    final int SERVER_PORT_NUMBER = 8888;

    public static void main(String[] args) {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.start();
    }

    public void start() {
        try (java.net.ServerSocket serverSocket = new java.net.ServerSocket(SERVER_PORT_NUMBER);) {
            while (true) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        // connection is made.
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {
                    System.out.println("Server Message: A Client has connected.");

                }
            }
        } catch (IOException e) {
            System.out.println("Server Message: IOException: " + e);
        }

    }
}
