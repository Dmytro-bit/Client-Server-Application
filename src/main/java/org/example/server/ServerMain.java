package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    static final int SERVER_PORT_NUMBER = 40000;

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT_NUMBER);
             ExecutorService fixedThreadPool = Executors.newFixedThreadPool(15)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getRemoteSocketAddress());

                fixedThreadPool.submit(new Server(clientSocket));
            }
        }
    }
}
