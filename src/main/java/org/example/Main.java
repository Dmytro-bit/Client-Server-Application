package org.example;

import org.example.DAOs.BaseSqlInterface;
import org.example.DAOs.MySqlBookingDao;
import org.example.DTOs.Booking;
import org.example.Exception.DaoException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

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

    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        int option = 0;

        List<Booking> allBookings = new ArrayList<>();

        do {
            System.out.println("----------MENU----------");
            System.out.println("1. View Bookings");
            System.out.println("2. View Booking By ID");
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