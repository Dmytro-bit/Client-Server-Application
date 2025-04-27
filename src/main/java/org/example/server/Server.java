package org.example.server;

import org.example.DTOs.Booking;
import org.example.Utils.JsonConverter;
import org.example.server.DAOs.BaseSqlInterface;
import org.example.server.DAOs.MySqlBookingDao;
import org.example.server.Exception.DaoException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Server implements Runnable {


    private final Socket clientSocket;

    public Server(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        while (true) {
            try (
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
            ) {
                BaseSqlInterface<Booking> baseDI = new MySqlBookingDao();
                DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                System.out.println("Database interface initialized.");

                String request;
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
                        case "3":
                            try {
                                JSONObject newBookingJson = new JSONObject(in.readLine());
                                Booking newBooking = new Booking();
                                newBooking.setInstanceFromJson(newBookingJson);

                                baseDI.insertEntity(newBooking);

                                System.out.println(newBooking);

                                out.println(JsonConverter.TableEntityToJson(newBooking));

                            } catch (Exception e) {
                                out.println("Something went wrong: " + e.getMessage());
                            }
                            break;
                        case "4":
                            try {
                                String idInput = in.readLine();
                                int id = Integer.parseInt(idInput);
                                int rowsAffected = baseDI.deleteEntity(id);
                                if (rowsAffected != 0) {
                                    out.println("Booking with ID " + id + " was deleted.");
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

                            if (!imageName.equals("*"))
                                sendFile("src/main/java/org/example/Images/" + imageName, dataOutputStream);
                            else
                                sendAllFiles("src/main/java/org/example/Images", dataOutputStream);
                            break;
                        case "0":
                            System.out.println("Client disconnected.");
                            clientSocket.close();
                            return;
                        default:
                            break;
                    }
                    out.flush();
                }
            } catch (DaoException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendFile(String fileName, DataOutputStream dataOutputStream) throws Exception {
        int numberOfBytes = 0;
        File file = new File(fileName);
        FileInputStream fileInputStream = new FileInputStream(file);

        dataOutputStream.writeLong(file.length());

        byte[] buffer = new byte[4 * 1024];
        while ((numberOfBytes = fileInputStream.read(buffer)) != -1) {

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
            sendFile("src/main/java/org/example/Images/" + file.getName(), dataOutputStream);
        }

    }

}