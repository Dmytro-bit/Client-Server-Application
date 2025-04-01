package org.example.server;

import org.example.DTOs.Booking;
import org.example.Utils.JsonConverter;
import org.example.server.DAOs.BaseSqlInterface;
import org.example.server.DAOs.MySqlBookingDao;
import org.example.server.Exception.DaoException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    final int SERVER_PORT_NUMBER = 8888;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT_NUMBER);) {
            while (true) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        // connection is made.
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {
                    System.out.println("Server Message: A Client has connected.");
                    BaseSqlInterface<Booking> baseDI = new MySqlBookingDao();

                    String request = in.readLine();

                    switch (request) {
                        case "1" :
                            List<Booking> allBookings = baseDI.getAllEntities();
                            out.println(JsonConverter.EntitiesToJson(allBookings));
                            break;
                    }

                } catch (DaoException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.out.println("Server Message: IOException: " + e);
        }

    }
}
