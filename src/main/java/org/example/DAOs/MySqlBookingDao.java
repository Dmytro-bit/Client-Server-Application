package org.example.DAOs;

import org.example.DTOs.Booking;
import org.example.Exception.DaoException;
import org.example.Utils.BookingStatus;

import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.regex.Pattern;

public class MySqlBookingDao extends MySqlDao implements BaseSqlInterface<Booking> {
    @Override
    public List<Booking> getAllEntities() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Booking> allBookings = new ArrayList<>();

        try {
            connection = getConnection();
            String query = "SELECT * FROM booking";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int customer_id = resultSet.getInt("customer_id");
                int table_id = resultSet.getInt("table_id");
                Date booking_date = resultSet.getDate("booking_date");
                Time start_time = resultSet.getTime("start_time");
                Time end_time = resultSet.getTime("end_time");
                String statusString = resultSet.getString("status");
                BookingStatus status;
                try {
                    status = BookingStatus.valueOf(statusString);
                } catch (IllegalArgumentException | NullPointerException e) {
                    System.err.println("Invalid booking status found: " + statusString);
                    status = BookingStatus.PENDING; // Fallback to a default status
                }

                allBookings.add(new Booking(id, customer_id, table_id, booking_date, start_time, end_time, status));
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) freeConnection(connection);
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
        return allBookings;
    }

    @Override
    public Booking getEntityById(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Booking booking = null;

        try {
            connection = getConnection();
            String query = "SELECT * FROM booking WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int ID = resultSet.getInt("id");
                int customer_id = resultSet.getInt("customer_id");
                int table_id = resultSet.getInt("table_id");
                Date booking_date = resultSet.getDate("booking_date");
                Time start_time = resultSet.getTime("start_time");
                Time end_time = resultSet.getTime("end_time");
                String statusString = resultSet.getString("status");
                BookingStatus status;
                try {
                    status = BookingStatus.valueOf(statusString);
                } catch (Exception e) {
                    System.err.println("Invalid booking status found: " + statusString);
                    status = BookingStatus.PENDING; // Fallback to a default status
                }
                booking = new Booking(ID, customer_id, table_id, booking_date, start_time, end_time, status);
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) freeConnection(connection);
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
        return booking;
    }

    @Override
    public void insertEntity(Booking b) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            String query = "Insert into booking values(?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, b.getId());
            preparedStatement.setInt(2, b.getCustomer_id());
            preparedStatement.setInt(3, b.getTable_id());
            preparedStatement.setDate(4, new java.sql.Date(b.getBookingDate().getTime()));
            preparedStatement.setTime(5, b.getStartTime());
            preparedStatement.setTime(6, b.getEndTime());
            preparedStatement.setString(7, b.getStringStatus());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) freeConnection(connection);
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
    }

    @Override
    public void updateEntity(int id, Booking booking) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            String query = "UPDATE Booking SET customer_id = ?,table_id = ?,booking_date = ?,start_time = ?,end_time = ?,status = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, booking.getCustomer_id());
            preparedStatement.setInt(2, booking.getTable_id());
            preparedStatement.setDate(3, new java.sql.Date(booking.getBookingDate().getTime()));
            preparedStatement.setTime(4, booking.getStartTime());
            preparedStatement.setTime(5, booking.getEndTime());
            preparedStatement.setString(6, booking.getStringStatus());
            preparedStatement.setInt(7, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) freeConnection(connection);

            } catch (SQLException e) {
                throw new DaoException("updateEntity() " + e.getMessage());
            }
        }
    }

    @Override
    public void deleteEntity(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            String query = "DELETE FROM booking WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) freeConnection(connection);

            } catch (SQLException e) {
                throw new DaoException("updateEntity() " + e.getMessage());
            }
        }
    }

    @Override
    public List<Booking> findEntitiesByFilter(Comparator<Booking> comparator) throws DaoException {
        List<Booking> all_bookings = getAllEntities();
        List<Booking> filtered_bookings = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select threshold time");
        String threshold = "";
        String regex = "^([01]\\d|2[0-3]):[0-5]\\d";
        Pattern pattern = Pattern.compile(regex);

        while (threshold.isEmpty() || !pattern.matcher(threshold).matches()) {
            threshold = scanner.nextLine();
        }


        Booking threshold_booking = new Booking(Time.valueOf(threshold+":00"));
        for (Booking booking : all_bookings) {
            if (comparator.compare(booking, threshold_booking) > 0) {
                filtered_bookings.add(booking);
            }
        }

        return filtered_bookings;
    }
}