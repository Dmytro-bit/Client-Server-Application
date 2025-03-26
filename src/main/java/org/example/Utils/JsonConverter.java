package org.example.Utils;

import org.example.DAOs.MySqlBookingDao;
import org.example.DAOs.MySqlCustomerDao;
import org.example.DAOs.MySqlTableDao;
import org.example.DTOs.Booking;
import org.example.DTOs.Customer;
import org.example.DTOs.RestaurantTable;
import org.example.Exception.DaoException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class JsonConverter {
    public static <T> JSONArray EntitiesToJson(List<T> entities) {
        return new JSONArray(entities);
    }

    public static JSONObject TableEntityToJson(int ID) throws DaoException {
        MySqlTableDao tableDao = new MySqlTableDao();
        RestaurantTable table = tableDao.getEntityById(ID);
        return new JSONObject(table);
    }

    public static JSONObject CustomerEntityToJson(int ID) throws DaoException {
        MySqlCustomerDao customerDao = new MySqlCustomerDao();
        Customer customer = customerDao.getEntityById(ID);
        return new JSONObject(customer);
    }

    public static JSONObject BookingEntityToJson(int ID) throws DaoException {
        MySqlBookingDao bookingDao = new MySqlBookingDao();
        Booking booking = bookingDao.getEntityById(ID);
        return new JSONObject(booking);
    }
}
