package org.example.server;

import org.example.DTOs.Booking;
import org.example.Utils.JsonConverter;
import org.example.server.DAOs.BaseSqlInterface;
import org.example.server.DAOs.MySqlBookingDao;
import org.example.server.Exception.DaoException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    final int SERVER_PORT_NUMBER = 40000;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT_NUMBER)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Server Message: A Client has connected.");

                try (
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                ) {
                    BaseSqlInterface<Booking> baseDI = new MySqlBookingDao();
                    DataOutputStream dataOutputStream = new DataOutputStream( clientSocket.getOutputStream() );
                    System.out.println("Database interface initialized.");

                    String request;
                    Scanner scanner = new Scanner(System.in);
                    while ((request = in.readLine()) != null) {
                        System.out.println("Request: " + request);
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
                            case "5":
                                String imageName = in.readLine();

                                if(!imageName.equals("*"))
                                    sendFile("C:/Users/dimab/Documents/Client-Server-Application/src/main/java/org/example/Images/"+imageName, dataOutputStream);
                                else
                                    sendAllFiles("C:/Users/dimab/Documents/Client-Server-Application/src/main/java/org/example/Images", dataOutputStream);
                                break;
                            case "0":
                                System.out.println("Client disconnected.");
                                clientSocket.close();
                                return;
                            default:
                                out.println("Invalid option.");
                        }
                    }
                } catch (DaoException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.out.println("Server Message: IOException: " + e);
        }
    }

    private void sendFile(String fileName, DataOutputStream dataOutputStream) throws Exception
    {
        int numberOfBytes = 0;
        File file = new File(fileName);
        FileInputStream fileInputStream = new FileInputStream(file);

        dataOutputStream.writeLong( file.length() );

        byte[] buffer = new byte[4 * 1024];
        while ((numberOfBytes = fileInputStream.read(buffer))!= -1) {

            dataOutputStream.write(buffer, 0, numberOfBytes);
            dataOutputStream.flush();
        }
        fileInputStream.close();
    }

    private void sendAllFiles(String directory, DataOutputStream dataOutputStream) throws Exception {
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        int fileCounter = 0;

        for (File file : listOfFiles) {
            if (file.isFile()) fileCounter++;
        }

        dataOutputStream.writeInt(fileCounter);

        for (File file : listOfFiles) {
            sendFile("C:/Users/dimab/Documents/Client-Server-Application/src/main/java/org/example/Images/" + file.getName(), dataOutputStream);
        }

    }

}
