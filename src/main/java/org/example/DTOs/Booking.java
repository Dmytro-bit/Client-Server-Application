package org.example.DTOs;

import org.example.Utils.BookingStatus;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Date;


public class Booking {
    private int id;
    private int customer_id;
    private int table_id;
    private Date bookingDate;
    private Time startTime;
    private Time endTime;
    private BookingStatus status;

    public Booking(int id, int customer_id, int table_id, Date bookingDate, Time startTime, Time endTime, BookingStatus status) {
        this.id = id;
        this.customer_id = customer_id;
        this.table_id = table_id;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Booking(int customer_id, int table_id, Date bookingDate, Time startTime, Time endTime, BookingStatus status) {
        this.customer_id = customer_id;
        this.table_id = table_id;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Booking(Time startTime) {
        this.startTime = startTime;
    }

    public Booking() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public String getStringStatus() {
        return status.toString();
    }


    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public void setInstanceFromJson(JSONObject bookingJson) {
        this.id = bookingJson.getInt("id");
        this.customer_id = bookingJson.getInt("customer_id");
        this.table_id = bookingJson.getInt("table_id");
        this.bookingDate = java.sql.Date.valueOf(bookingJson.getString("bookingDate"));
        this.startTime = java.sql.Time.valueOf(bookingJson.getString("startTime"));
        this.endTime = java.sql.Time.valueOf(bookingJson.getString("endTime"));
        this.status = BookingStatus.PENDING;
    }

    @Override
    public String toString() {
        return String.format(
                "Booking - ID: %2d | Customer ID: %2d | Table ID: %2d | Booking Date: %s | Start Time: %s | End Time: %s | Status: %s ",
                id, customer_id, table_id, bookingDate, startTime, endTime, status
        );
    }
}