package org.example.DTOs;

import org.example.Utils.BookingStatus;

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


    public Booking(Time startTime) {
        this.startTime = startTime;
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

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", customer_id=" + customer_id +
                ", table_id=" + table_id +
                ", bookingDate=" + bookingDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                '}';
    }
}