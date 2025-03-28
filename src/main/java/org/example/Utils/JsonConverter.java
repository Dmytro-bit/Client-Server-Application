package org.example.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class JsonConverter {
    public static <T> String EntitiesToJson(List<T> entities) {
        return new JSONArray(entities).toString();
    }

    public static <T> String TableEntityToJson(T object) {
        return new JSONObject(object).toString();
    }
}
