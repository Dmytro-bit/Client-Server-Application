package org.example.server;

import org.example.DTOs.Booking;
import org.example.DTOs.RestaurantTable;
import org.example.Utils.BookingStatus;
import org.example.Utils.JsonConverter;
import org.example.server.DAOs.BaseSqlInterface;
import org.example.server.DAOs.MySqlBookingDao;
import org.example.server.DAOs.MySqlTableDao;
import org.example.server.Exception.DaoException;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void displayAllBookings(List<Booking> bookingList) {
        for (Booking booking : bookingList) {
            System.out.println(booking.toString());
        }
    }

    public static void displayAllTables(List<RestaurantTable> tableList) {
        for (RestaurantTable table : tableList) {
            System.out.println(table.toString());
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

    public static void displayAllTables() throws DaoException {
        BaseSqlInterface<RestaurantTable> baseDI = new MySqlTableDao();
        try {
            List<RestaurantTable> allTables = baseDI.getAllEntities();

            for (RestaurantTable booking : allTables) {
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

        try {
            System.out.println("Enter ID (Valid Integer):");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid integer:");
                scanner.next();
            }
            int ID = scanner.nextInt();

            booking = baseDI.getEntityById(ID);
            if (booking != null) {
                System.out.println(booking.toString());
            } else {
                System.out.println("No booking found with ID: " + ID);
            }
        } catch (DaoException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public static void AddBooking() throws DaoException {
        Scanner addScanner = new Scanner(System.in);

        try {
            LocalDate today = LocalDate.now();
            Date bookingDate = Date.valueOf(today);


            System.out.println("Enter Customer ID:");
            while (!addScanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid integer:");
                addScanner.next();
            }
            int customerId = addScanner.nextInt();

            System.out.println("Enter Table ID:");
            while (!addScanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid integer:");
                addScanner.next();
            }
            int tableId = addScanner.nextInt();
            addScanner.nextLine();

            System.out.println("Enter Start Time (HH:mm):");
            String startTimeString = "";
            while (true) {
                startTimeString = addScanner.nextLine().trim();
                if (startTimeString.matches("^(?:[01]\\d|2[0-3]):(?:00|30)$")) {
                    break;
                } else {
                    System.out.println("Invalid time format. Please enter a valid time in HH:mm format (e.g., 10:00 or 14:30):");
                }
            }
            Time startTime = Time.valueOf(startTimeString + ":00");

            System.out.println("Enter End Time (HH:mm):");
            String endTimeString = "";
            while (true) {
                endTimeString = addScanner.nextLine().trim();
                if (endTimeString.matches("^(?:[01]\\d|2[0-3]):(?:00|30)$")) {
                    Time endTime = Time.valueOf(endTimeString + ":00");
                    if (endTime.after(startTime)) {
                        break;
                    } else {
                        System.out.println("End time must be later than start time. Please enter a valid end time:");
                    }
                } else {
                    System.out.println("Invalid time format. Please enter a valid time in HH:mm format (e.g., 10:00 or 14:30):");
                }
            }
            Time endTime = Time.valueOf(endTimeString + ":00");

            System.out.println("Enter Booking Status (PENDING, CONFIRMED, CANCELED):");
            String statusString = addScanner.next().toUpperCase();
            BookingStatus status;
            try {
                status = BookingStatus.valueOf(statusString);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid status. Please enter one of the following: PENDING, CONFIRMED, CANCELED.");
                return;
            }

            Booking newBooking = new Booking(customerId, tableId, bookingDate, startTime, endTime, status);
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

        try {
            MySqlBookingDao dao = new MySqlBookingDao();
            Booking booking = null;
            int id;

            while (true) {
                System.out.println("Enter Booking ID:");
                while (!updateScanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a valid integer:");
                    updateScanner.next();
                }
                id = updateScanner.nextInt();

                booking = dao.getEntityById(id);
                if (booking == null) {
                    System.out.println("No booking found with ID: " + id);
                } else {
                    break;
                }
            }

            System.out.println("Current Booking Details: " + booking.toString());

            System.out.println("Enter Customer ID:");
            while (!updateScanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid integer:");
                updateScanner.next();
            }
            int customerId = updateScanner.nextInt();
            booking.setCustomer_id(customerId);

            System.out.println("Enter Table ID:");
            while (!updateScanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid integer:");
                updateScanner.next();
            }
            int tableId = updateScanner.nextInt();
            booking.setTable_id(tableId);
            updateScanner.nextLine();

            System.out.println("Enter Date (YYYY-MM-DD):");
            String dateInput;
            while (true) {
                dateInput = updateScanner.nextLine().trim();
                try {
                    booking.setBookingDate(Date.valueOf(dateInput));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid date format. Please enter a valid date in YYYY-MM-DD format:");
                }
            }

            System.out.println("Enter Start Time (HH:mm):");
            String startTimeString;
            while (true) {
                startTimeString = updateScanner.nextLine().trim();
                if (startTimeString.matches("^(?:[01]\\d|2[0-3]):(?:00|30)$")) {
                    booking.setStartTime(Time.valueOf(startTimeString + ":00"));
                    break;
                } else {
                    System.out.println("Invalid time format. Please enter a valid time in HH:mm format (e.g., 10:00 or 14:30):");
                }
            }

            System.out.println("Enter End Time (HH:mm):");
            String endTimeString;
            while (true) {
                endTimeString = updateScanner.nextLine().trim();
                if (endTimeString.matches("^(?:[01]\\d|2[0-3]):(?:00|30)$")) {
                    Time endTime = Time.valueOf(endTimeString + ":00");
                    if (endTime.after(booking.getStartTime())) {
                        booking.setEndTime(endTime);
                        break;
                    } else {
                        System.out.println("End time must be later than start time. Please enter a valid end time:");
                    }
                } else {
                    System.out.println("Invalid time format. Please enter a valid time in HH:mm format (e.g., 10:00 or 14:30):");
                }
            }

            System.out.println("Enter Booking Status (PENDING, CONFIRMED, CANCELED):");
            String statusString;
            while (true) {
                statusString = updateScanner.next().toUpperCase();
                try {
                    booking.setStatus(BookingStatus.valueOf(statusString));
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid status. Please enter one of the following: PENDING, CONFIRMED, CANCELED:");
                }
            }

            dao.updateEntity(id, booking);
            System.out.println("Booking updated successfully!");
        } catch (Exception e) {
            System.err.println("Error updating booking. Please check your input.");
            e.printStackTrace();
        }
    }

    public static void deleteBooking() throws DaoException {
        MySqlBookingDao dao = new MySqlBookingDao();
        Scanner deleteScanner = new Scanner(System.in);

        try {
            Booking booking = null;
            int id;

            while (true) {
                System.out.println("Enter Booking ID:");
                while (!deleteScanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a valid integer:");
                    deleteScanner.next();
                }
                id = deleteScanner.nextInt();

                booking = dao.getEntityById(id);
                if (booking == null) {
                    System.out.println("No booking found with ID: " + id);
                } else {
                    break;
                }
            }

            dao.deleteEntity(id);
            System.out.println("Booking deleted successfully!");
        } catch (Exception e) {
            System.err.println("Error deleting booking. Please check your input.");
            e.printStackTrace();
        }
    }

    public static void findBookingByFilter() throws DaoException {
        MySqlBookingDao dao = new MySqlBookingDao();

//        Comparator<Booking> bookingComparator = (b1, b2) -> b1.getStartTime().compareTo(b2.getStartTime());
        Comparator<Booking> bookingComparator = Comparator.comparing(Booking::getStartTime);

        List<Booking> filtered_bookings = dao.findEntitiesByFilter(bookingComparator);

        System.out.println("Found " + filtered_bookings.size() + " bookings");
        displayAllBookings(filtered_bookings);

    }

    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        String option = "";

        do {
            System.out.println("\n----------MENU----------");
            System.out.println("1. View Bookings");
            System.out.println("2. View Booking By ID");
            System.out.println("3. Add Booking");
            System.out.println("4. Update Booking");
            System.out.println("5. Delete Booking");
            System.out.println("6. Filter by comparator");
            System.out.println("*----TABLE----*");
            System.out.println("7. View Tables");
            System.out.println("8. View Table By ID");
            System.out.println("9. Add Table");
            System.out.println("10. Update Table");
            System.out.println("11. Delete Table");
            System.out.println("12. See Table with Enough Sits");
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
                    case "0":
                        System.out.println("Exiting the program...");
                        break;
                    case "7":
                        displayAllTables();
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (DaoException e) {
                throw new RuntimeException(e);
            }

        } while (!option.equals("0"));
    }

    public static void main(String[] args) throws DaoException {
        menu();
        MySqlTableDao baseDI = new MySqlTableDao();

        List<RestaurantTable> list = baseDI.getEntitiesByField("", "22:00:00");
    }
}