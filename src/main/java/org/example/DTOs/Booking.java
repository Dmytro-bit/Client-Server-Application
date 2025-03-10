package org.example.DTOs;

import org.example.Utils.BookingStatus;

import java.time.LocalDate;
import java.time.LocalTime;


public class Booking {
    private int id;
    private int customer_id;
    private int table_id;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus status;

    public Booking(int id, int customer_id, int table_id, LocalDate bookingDate, LocalTime startTime, LocalTime endTime, BookingStatus status) {
        this.id = id;
        this.customer_id = customer_id;
        this.table_id = table_id;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Booking(int customer_id, int table_id, LocalDate bookingDate, LocalTime startTime, LocalTime endTime, BookingStatus status) {
        this.customer_id = customer_id;
        this.table_id = table_id;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
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

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
