package org.example;

import org.example.DAOs.BaseSqlInterface;
import org.example.DAOs.MySqlBookingDao;
import org.example.DTOs.Booking;
import org.example.Exception.DaoException;
import org.example.Utils.BookingStatus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

public class Main {

    public static List<Booking> displayAllBookings() throws DaoException {
        BaseSqlInterface baseDI = new MySqlBookingDao();
        List<Booking> allBookings = new ArrayList<>();
        try {
            allBookings = baseDI.getAllEntities();

            for (Booking booking : allBookings) {
                System.out.println(booking.toString());
            }
        }catch (DaoException e) {
            e.printStackTrace();
        }

        return allBookings;
    }

    public static Booking displayBookingByID() throws DaoException {
        BaseSqlInterface baseDI = new MySqlBookingDao();
        Booking booking;
        Scanner scanner = new Scanner(System.in);
        int ID = 0;

        try{
            System.out.println("Enter ID (Valid Integer)");
            ID = scanner.nextInt();
            booking = (Booking) baseDI.getEntityById(ID);
            System.out.println(booking.toString());
        } catch (DaoException e) {
            throw new DaoException(e.getMessage());
        } catch (InputMismatchException e) {
            throw new InputMismatchException("Integer must be an integer");
        }
        return booking;
    }

    public static void AddBooking() throws DaoException {
        Scanner addScanner = new Scanner(System.in);

        try {
            LocalDate today = LocalDate.now();
            Date bookingDate = Date.valueOf(today);

            System.out.println("Enter Booking ID:");
            int id = addScanner.nextInt();

            System.out.println("Enter Customer ID:");
            int customerId = addScanner.nextInt();

            System.out.println("Enter Table ID:");
            int tableId = addScanner.nextInt();

            System.out.println("Enter Start Time (HH:mm:ss):");
            String startTimeString = addScanner.next();
            Time startTime = Time.valueOf(startTimeString);

            System.out.println("Enter End Time (HH:mm:ss):");
            String endTimeString = addScanner.next();
            Time endTime = Time.valueOf(endTimeString);

            System.out.println("Enter Booking Status (PENDING, CONFIRMED, CANCELED):");
            String statusString = addScanner.next().toUpperCase();
            BookingStatus status = BookingStatus.valueOf(statusString);

            Booking newBooking = new Booking(id, customerId, tableId, bookingDate, startTime, endTime, status);

            BaseSqlInterface baseDI = new MySqlBookingDao();
            baseDI.insertEntity(newBooking);

            System.out.println("Booking added successfully!");
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input. Please check the values you entered.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        int option = 0;

        do {
            System.out.println("----------MENU----------");
            System.out.println("1. View Bookings");
            System.out.println("2. View Booking By ID");
            System.out.println("3. Add Booking");
            System.out.println("0. Exit");

            try{
                System.out.println("Choose an option: ");
                option = scanner.nextInt();

                switch (option) {
                    case 1:
                        displayAllBookings();
                        break;
                    case 2:
                        displayBookingByID();
                        break;
                    case 3:
                        AddBooking();
                        break;
                }
            } catch (DaoException e) {
                throw new RuntimeException(e);
            }

        }while (option != 0);
    }

    public static void main(String[] args) {
        menu();
    }
}