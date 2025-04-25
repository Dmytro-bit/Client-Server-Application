package org.example.client;

import java.io.*;
import java.net.Socket;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Scanner;

public class Client {

    private DataInputStream dataInputStream = null;

    public static void main(String[] args) {
        Client clientSocket = new Client();
        clientSocket.start();
    }

    public void start() {
        try (Socket socket = new Socket("localhost", 40000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
            Scanner scanner = new Scanner(System.in);
            String userRequest = "";

            do {
                System.out.println("\n----------MENU----------");
                System.out.println("1. View Bookings");
                System.out.println("2. View Booking By ID");
                System.out.println("5. View Images to Download");
                System.out.println("0. Exit");


                System.out.println("\nChoose an option: ");
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
                    case "2":
                        System.out.println("Enter Booking ID: ");
                        String bookingId = scanner.nextLine();
                        out.println(bookingId);

                        String response = in.readLine();
                        if (response.startsWith("{")) {
                            JSONObject booking = new JSONObject(response);
                            System.out.println("Booking Details:");
                            System.out.printf("| %-2s | %-12s | %-10s | %-10s | %-8s | %-12s | %-10s |\n", "ID", "Booking Date", "Start Time", "End Time", "Table_ID", "Customer_ID", "Status");
                            System.out.printf("| %-2d | %-12s | %-10s | %-10s | %-8d | %-12d | %-10s |\n",
                                    booking.getInt("id"),
                                    booking.getString("bookingDate"),
                                    booking.getString("startTime"),
                                    booking.getString("endTime"),
                                    booking.getInt("table_id"),
                                    booking.getInt("customer_id"),
                                    booking.getString("status"));
                        } else {
                            System.out.println(response);
                        }
                        break;
                    case "5":
                        dataInputStream = new DataInputStream(socket.getInputStream());
                        System.out.println("Choose image:");
                        File folder = new File("C:/Users/dimab/Documents/Client-Server-Application/src/main/java/org/example/Images");
                        File[] listOfFiles = folder.listFiles();
                        if(listOfFiles != null) {
                            for (int i = 0; i < listOfFiles.length; i++) {
                                if (listOfFiles[i].isFile()) {
                                    System.out.println((i+1) + ". " + listOfFiles[i].getName());
                                } else if (listOfFiles[i].isDirectory()) {
                                    System.out.println("Error reading file name. Selected file is a directory." );
                                }
                            }
                        }
                        System.out.println((listOfFiles.length + 1) + ". All images");
                        System.out.println("Choose image:");
                        String imageName = scanner.nextLine();
                        out.println(imageName);
                        dataInputStream = new DataInputStream(socket.getInputStream());

                        System.out.println("Server is waiting for image data to arrive...");

                        receiveFile("C:/Users/dimab/Documents/Client-Server-Application/src/main/java/org/example/Images/lambert_received.png");
                        break;
                    case "0":

                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }

            } while (!userRequest.equals("0"));

        } catch (IOException e) {
            System.out.println("Client Message: IOException: " + e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void receiveFile(String fileName) throws Exception
    {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        long numberOfBytesRemaining = dataInputStream.readLong();
        System.out.println("Server: size of image file (in bytes) = " + numberOfBytesRemaining);

        byte[] buffer = new byte[4*1024];
        System.out.println("Buffer size: " + (4*1024) + " bytes");
        int numberOfBytesRead = 0;

        while (numberOfBytesRemaining > 0 &&  (numberOfBytesRead =
                dataInputStream.read(buffer, 0,(int)Math.min(buffer.length, numberOfBytesRemaining))) != -1) {

            fileOutputStream.write(buffer, 0, numberOfBytesRead);

            numberOfBytesRemaining = numberOfBytesRemaining - numberOfBytesRead;

            System.out.print("Bytes read: " + numberOfBytesRead);
            System.out.println(" - Bytes remaining: " + numberOfBytesRemaining);
        }
        fileOutputStream.close();

        System.out.println("File was Received");
    }
}
