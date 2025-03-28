package org.example.server.DAOs;

import org.example.DTOs.RestaurantTable;
import org.example.server.Exception.DaoException;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MySqlTableDao extends MySqlDao implements BaseSqlInterface<RestaurantTable> {
    @Override
    public List<RestaurantTable> getAllEntities() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<RestaurantTable> tables = new ArrayList<>();

        try {
            connection = getConnection();
            String query = "select * from RestaurantTable";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int capacity = resultSet.getInt("capacity");

                tables.add(new RestaurantTable(id, capacity));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) freeConnection(connection);
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
        return tables;
    }

    @Override
    public RestaurantTable getEntityById(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        RestaurantTable table = null;

        try {
            connection = getConnection();
            String query = "SELECT * FROM RestaurantTable WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int ID = resultSet.getInt("id");
                int capacity = resultSet.getInt("capacity");
                table = new RestaurantTable(ID, capacity);
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
        return table;
    }

    @Override
    public List<RestaurantTable> getEntitiesByField(String field, String value) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<RestaurantTable> tables = new ArrayList<>();

        try {
            connection = getConnection();
            String query = "SELECT DISTINCT t.id, t.capacity " +
                    "FROM RestaurantTable t " +
                    "WHERE t.id NOT IN (" +
                    "   SELECT b.table_id FROM Booking b " +
                    "   WHERE ? BETWEEN b.start_time AND b.end_time AND b.status != 'cancelled'" +
                    ");";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setTime(1, Time.valueOf(LocalTime.parse(value, DateTimeFormatter.ofPattern("HH:mm:ss"))));

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int capacity = resultSet.getInt("capacity");

                tables.add(new RestaurantTable(id, capacity));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) freeConnection(connection);
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }

        return tables;
    }

    @Override
    public void insertEntity(RestaurantTable table) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            String query = "Insert into RestaurantTable values(?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, table.getId());
            preparedStatement.setInt(1, table.getCapacity());

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
    public void updateEntity(int id, RestaurantTable restaurantTable) throws DaoException {

    }

    @Override
    public void deleteEntity(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            String query = "DELETE FROM RestaurantTable WHERE id = ?";
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
                throw new DaoException("deleteEntity() " + e.getMessage());
            }
        }

    }

    @Override
    public List<RestaurantTable> findEntitiesByFilter(Comparator<RestaurantTable> comparator) throws DaoException {
        return null;
    }
}
