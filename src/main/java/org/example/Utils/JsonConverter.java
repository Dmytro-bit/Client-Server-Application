package org.example.Utils;

import org.example.Exception.DaoException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class JsonConverter {
    public static <T> JSONArray EntitiesToJson(List<T> entities) {
        return new JSONArray(entities);
    }

    public static <T> JSONObject TableEntityToJson(T object) throws DaoException {
        return new JSONObject(object);
    }
}
