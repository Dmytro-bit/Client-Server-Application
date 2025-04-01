package org.example.client;

import org.example.DTOs.Booking;
import org.example.Utils.JsonConverter;
import org.example.server.Exception.DaoException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Scanner;

public class Client {
    final int SERVER_PORT_NUMBER = 8888;

    public static void main(String[] args) {
        Client clientSocket = new Client();
        clientSocket.start();
    }

    public void start() {
        try (Socket socket = new Socket("localhost", 8888);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
            Scanner scanner = new Scanner(System.in);
            String userRequest = "";

            do {
                System.out.println("----------MENU----------");
                System.out.println("1. View Bookings");
                System.out.println("2. View Booking By ID");
                System.out.println("0. Exit");


                System.out.println("Choose an option: ");
                userRequest = scanner.nextLine();
                out.println(userRequest);
                switch (userRequest) {
                    case "1":
                        String res = in.readLine();
                        JSONArray bookings = new JSONArray(res);
                        System.out.println("Response:");
                        System.out.printf("| %-2s | %-12s | %-10s | %-10s | %-8s | %-12s | %-10s |\n", "ID", "Booking Date", "Start Time", "End Time", "Table_ID", "Customer_ID", "Status");
                        for (int i = 0; i < bookings.length(); i++) {
                            JSONObject booking = bookings.getJSONObject(i);
                            System.out.printf("| %-2d | %-12s | %-10s | %-10s | %-8d | %-12d | %-10s |\n",
                                    booking.getInt("id"),
                                    booking.getString("bookingDate"),
                                    booking.getString("startTime"),
                                    booking.getString("endTime"),
                                    booking.getInt("table_id"),
                                    booking.getInt("customer_id"),
                                    booking.getString("status"));
                        }
                        break;
                    case "0":

                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }

            } while (!userRequest.equals("0"));

        } catch (IOException e) {
            System.out.println("Client Message: IOException: " + e);
        }
    }


}
