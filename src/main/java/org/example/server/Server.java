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
    final int SERVER_PORT_NUMBER = 40000;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT_NUMBER)) {
            while (true) { // Always listen for new clients
                Socket clientSocket = serverSocket.accept();
                System.out.println("Server Message: A Client has connected.");

                try (
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                ) {
                    BaseSqlInterface<Booking> baseDI = new MySqlBookingDao();

                    String request;
                    while ((request = in.readLine()) != null) { // Keep listening for requests
                        switch (request) {
                            case "1":
                                List<Booking> allBookings = baseDI.getAllEntities();
                                out.println(JsonConverter.EntitiesToJson(allBookings));
                                break;
                            case "2":
                                try {
                                    String idInput = in.readLine();
                                    int id = Integer.parseInt(idInput);

                                    Booking booking = baseDI.getEntityById(id);

                                    if (booking != null) {
                                        out.println(JsonConverter.TableEntityToJson(booking));
                                    } else {
                                        out.println("Booking with ID " + id + " was not found.");
                                    }
                                } catch (NumberFormatException e) {
                                    out.println("Invalid ID format.");
                                } catch (DaoException e) {
                                    e.printStackTrace();
                                    out.println("Error retrieving entity: " + e.getMessage());
                                }
                                break;
                            case "0":
                                System.out.println("Client disconnected.");
                                clientSocket.close(); // Close when the client exits
                                return;
                            default:
                                out.println("Invalid option.");
                        }
                    }
                } catch (DaoException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Server Message: IOException: " + e);
        }
    }
}
