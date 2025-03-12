package org.example;

import org.example.DAOs.BaseSqlInterface;
import org.example.DAOs.MySqlBookingDao;
import org.example.DTOs.Booking;
import org.example.Exception.DaoException;
import org.example.Utils.BookingStatus;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void displayAllBookings(List<Booking> bookingList) {
        for (Booking booking : bookingList) {
            System.out.println(booking.toString());
        }
    }

    public static void displayAllBookings() throws DaoException {
        BaseSqlInterface<Booking> baseDI = new MySqlBookingDao();
        try {
            List<Booking> allBookings = baseDI.getAllEntities();

            for (Booking booking : allBookings) {
                System.out.println(booking.toString());
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    public static void displayBookingByID() throws DaoException {
        BaseSqlInterface<Booking> baseDI = new MySqlBookingDao();
        Booking booking;
        Scanner scanner = new Scanner(System.in);
        int ID = 0;

        try {
            System.out.println("Enter ID (Valid Integer)");
            ID = scanner.nextInt();
            booking = baseDI.getEntityById(ID);
            System.out.println(booking.toString());
        } catch (DaoException e) {
            throw new DaoException(e.getMessage());
        } catch (InputMismatchException e) {
            throw new InputMismatchException("Integer must be an integer");
        }
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

            BaseSqlInterface<Booking> baseDI = new MySqlBookingDao();
            baseDI.insertEntity(newBooking);

            System.out.println("Booking added successfully!");
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input. Please check the values you entered.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void UpdateBooking() throws DaoException {
        Scanner updateScanner = new Scanner(System.in);

        System.out.println("Enter Booking ID:");
        int id = updateScanner.nextInt();

        MySqlBookingDao dao = new MySqlBookingDao();
        Booking booking = dao.getEntityById(id);
        System.out.println(booking.toString());

        Object input;

        System.out.println("Enter Customer ID:");
        input = updateScanner.nextInt();
        booking.setCustomer_id((int) input);

        System.out.println("Enter Table ID:");
        input = updateScanner.nextInt();
        booking.setTable_id((int) input);
        updateScanner.nextLine();

        System.out.println("Enter Date YYYY-MM-DD:");
        input = updateScanner.nextLine();
        booking.setBookingDate(Date.valueOf((String) input));

        System.out.println("Enter Start Time (HH:mm:ss):");
        input = updateScanner.nextLine();
        booking.setStartTime(Time.valueOf((String) input));

        System.out.println("Enter End Time (HH:mm:ss):");
        input = updateScanner.nextLine();
        booking.setEndTime(Time.valueOf((String) input));

        System.out.println("Enter Booking Status (PENDING, CONFIRMED, CANCELED):");
        input = updateScanner.next().toUpperCase();
        booking.setStatus(BookingStatus.valueOf((String) input));


        dao.updateEntity(id, booking);
        System.out.println("Booking updated successfully!");
    }

    public static void deleteBooking() throws DaoException {
        MySqlBookingDao dao = new MySqlBookingDao();
        Scanner deleteScanner = new Scanner(System.in);
        System.out.println("Enter Booking ID:");
        int id = deleteScanner.nextInt();

        dao.deleteEntity(id);
        System.out.println("Booking deleted successfully!");
    }

    public static void findBookingByFilter() throws DaoException {
        MySqlBookingDao dao = new MySqlBookingDao();

        Comparator<Booking> bookingComparator = (b1, b2) -> b1.getStartTime().compareTo(b2.getStartTime());

        List<Booking> filtered_bookings = dao.findEntitiesByFilter(bookingComparator);

        System.out.println("Found " + filtered_bookings.size() + " bookings");
        displayAllBookings(filtered_bookings);

    }

    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        String option = "";

        do {
            System.out.println("----------MENU----------");
            System.out.println("1. View Bookings");
            System.out.println("2. View Booking By ID");
            System.out.println("3. Add Booking");
            System.out.println("4. Update Booking");
            System.out.println("5. Delete Booking");
            System.out.println("6. Filter by comparator");
            System.out.println("0. Exit");

            try {
                System.out.println("Choose an option: ");
                option = scanner.nextLine();

                switch (option) {
                    case "1":
                        displayAllBookings();
                        break;
                    case "2":
                        displayBookingByID();
                        break;
                    case "3":
                        AddBooking();
                        break;
                    case "4":
                        UpdateBooking();
                        break;
                    case "5":
                        deleteBooking();
                        break;
                    case "6":
                        findBookingByFilter();
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (DaoException e) {
                throw new RuntimeException(e);
            }

        } while (!option.equals("0"));
    }

    public static void main(String[] args) {
        menu();
    }
}