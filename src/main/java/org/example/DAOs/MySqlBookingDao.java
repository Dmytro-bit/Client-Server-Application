package org.example.DAOs;

import org.example.Exception.DaoException;

import org.example.DTOs.Booking;
import org.example.Utils.BookingStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MySqlBookingDao extends MySqlDao implements BaseSqlInterface{
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

            while(resultSet.next())
            {
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
        }catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            try {
                if(resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if(connection != null) connection.close();
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

        try{
            connection = getConnection();
            String query = "SELECT * FROM booking WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
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
                if(resultSet != null) resultSet.close();
                if(preparedStatement != null) preparedStatement.close();
                if(connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
        return booking;
    }

    @Override
    public Booking insertEntity(Object o) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Booking booking = (Booking) o;

        try {
            connection = getConnection();
            String query = "Insert into booking values(?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, ((Booking) o).getId());
            preparedStatement.setInt(2, ((Booking) o).getCustomer_id());
            preparedStatement.setInt(3, ((Booking) o).getTable_id());
            preparedStatement.setDate(4, new java.sql.Date(((Booking) o).getBookingDate().getTime()));
            preparedStatement.setTime(5, ((Booking) o).getStartTime());
            preparedStatement.setTime(6, ((Booking) o).getEndTime());
            preparedStatement.setString(7, String.valueOf(((Booking) o).getStatus()));

            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(preparedStatement != null) preparedStatement.close();
                if(connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
        return booking;
    }

    @Override
    public void updateEntity(int id, Object o) {

    }

    @Override
    public void deleteEntity(int id) throws DaoException {

    }

    @Override
    public List findEntitiesByFilter(Comparator comparator) throws DaoException {
        return List.of();
    }
}