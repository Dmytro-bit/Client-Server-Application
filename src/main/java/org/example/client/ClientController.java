package org.example.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class ClientController {
    @FXML
    private TextField bookingIdField;

    @FXML
    private TextArea outputArea;

    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    private Stage stage;

    @FXML
    public void initialize() {
        try {
            socket = new Socket("localhost", 40000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            outputArea.setText("Connection failed: " + e.getMessage());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void handleViewBookings() {
        new Thread(() -> {
            try {
                out.println("1");
                String response = in.readLine();
                JSONArray bookings = new JSONArray(response);

                StringBuilder output = new StringBuilder("Bookings:\n");
                for (int i = 0; i < bookings.length(); i++) {
                    JSONObject booking = bookings.getJSONObject(i);
                    output.append(String.format("ID:%d | Date:%s | Start:%s | End:%s | Table:%d | Customer:%d | Status:%s\n",
                            booking.getInt("id"),
                            booking.getString("bookingDate"),
                            booking.getString("startTime"),
                            booking.getString("endTime"),
                            booking.getInt("table_id"),
                            booking.getInt("customer_id"),
                            booking.getString("status")));
                }

                Platform.runLater(() -> outputArea.setText(output.toString()));

            } catch (Exception e) {
                Platform.runLater(() -> outputArea.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    public void handleViewById() {
        String id = bookingIdField.getText().trim();
        if (id.isEmpty()) return;

        new Thread(() -> {
            try {
                out.println("2");
                Thread.sleep(100); // Give server time to switch state
                out.println(id);
                String response = in.readLine();

                if (response.startsWith("{")) {
                    JSONObject booking = new JSONObject(response);
                    String formatted = String.format("ID:%d | Date:%s | Start:%s | End:%s | Table:%d | Customer:%d | Status:%s",
                            booking.getInt("id"),
                            booking.getString("bookingDate"),
                            booking.getString("startTime"),
                            booking.getString("endTime"),
                            booking.getInt("table_id"),
                            booking.getInt("customer_id"),
                            booking.getString("status"));

                    Platform.runLater(() -> outputArea.setText(formatted));
                } else {
                    Platform.runLater(() -> outputArea.setText(response));
                }
            } catch (Exception e) {
                Platform.runLater(() -> outputArea.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    public void clearField() {
        Platform.runLater(() -> outputArea.clear());
    }

    public void closeConnection() {
        try {
            if (out != null) out.println("0");
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}

        if (stage != null) {
            Platform.runLater(stage::close);
        }
    }
}
