package org.example.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.DTOs.Booking;
import org.example.Utils.JsonConverter;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.sql.Time;

public class AddBookingController {
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    private Stage stage;

    @FXML
    private TextField customerIdField;
    @FXML
    private TextField tableIdField;
    @FXML
    private DatePicker bookingDateField;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setConnection(Socket socket, PrintWriter out, BufferedReader in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
    }

    public void closeWindow() {

        if (stage != null) {
            Platform.runLater(stage::close);
        }
    }

    public void AddBooking() throws IOException {
        Booking booking = new Booking();
        booking.setCustomer_id(Integer.parseInt(customerIdField.getText()));
        booking.setTable_id(Integer.parseInt(tableIdField.getText()));
        booking.setBookingDate(Date.valueOf(bookingDateField.getValue()));
        booking.setStartTime(Time.valueOf(startTimeField.getText()+":00"));
        booking.setEndTime(Time.valueOf(endTimeField.getText()+":00"));

        out.println(JsonConverter.TableEntityToJson(booking));

        String response = in.readLine();

        System.out.println(response);

        Booking responce_booking = new Booking();
        JSONObject newBookingJson = new JSONObject(response);
        responce_booking.setInstanceFromJson(newBookingJson);
        closeWindow();

    }
}