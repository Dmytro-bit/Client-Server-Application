package org.example.DAOs;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import org.example.DTOs.RestaurantTable;
import org.example.Exception.DaoException;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MySqlTableDao extends MySqlDao implements BaseSqlInterface{
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

            while(resultSet.next()) {
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
    public Object getEntityById(int id) throws DaoException {
        return null;
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

            while(resultSet.next()) {
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
    public void insertEntity(Object o) {

    }

    @Override
    public void updateEntity(int id, Object o) throws DaoException {

    }

    @Override
    public void deleteEntity(int id) throws DaoException {

    }

    @Override
    public List findEntitiesByFilter(Comparator comparator) throws DaoException {
        return null;
    }
}
