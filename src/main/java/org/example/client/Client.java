package org.example.client;

import org.example.DTOs.Booking;
import org.example.Utils.JsonConverter;
import org.example.server.Exception.DaoException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
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
            String option = "";

            do {
                System.out.println("----------MENU----------");
                System.out.println("1. View Bookings");
                System.out.println("2. View Booking By ID");
                System.out.println("0. Exit");


                System.out.println("Choose an option: ");
                String userRequest = scanner.nextLine();
                out.println(userRequest);
                switch (option) {
                    case "1":
                        String res = in.readLine();
                        System.out.println("Response:");
                        System.out.println(res);
                        break;
                    case "2":

                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }

            } while (true);

        } catch (IOException e) {
            System.out.println("Client Message: IOException: " + e);
        }
    }


}
