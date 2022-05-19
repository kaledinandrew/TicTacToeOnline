package org.TicTacToeCLI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class JsonParser {

    public static Map<String, Object> parseJSON(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(jsonString, new TypeReference<HashMap<String,Object>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("Не удалось прочитать JSON");
            System.exit(1);
        }
        return map;
    }

    public static String MapToString(Map<String, Object> parameters) {
        StringBuilder requestData = new StringBuilder();

        requestData.append("{");
        for (Map.Entry param : parameters.entrySet()) {
            if (requestData.length() != 1) {
                requestData.append(", ");
            }
            // Encode the parameter based on the parameter map we've defined
            // and append the values from the map to form a single parameter
            try {
                requestData.append("\"");
                requestData.append(URLEncoder.encode((String) param.getKey(), "UTF-8"));
                requestData.append("\"");
                requestData.append(": ");
                requestData.append("\"");
                requestData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                requestData.append("\"");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                System.out.println("Encoding error (parser)");
                System.exit(1);
            }
        }
        requestData.append("}");

        return requestData.toString();
    }

    public static ArrayList<ArrayList<Integer>> parseField(String stringList) {
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();

        for (String l: stringList.split("\\]\\s*,\\s*\\[")) {
            l = l.replaceAll("\\[", "").replaceAll("\\]", "").toString();
            matrix.add(new ArrayList<Integer>());
            for (String s : l.split(", ")) {
                matrix.get(matrix.size() - 1).add(Integer.valueOf(s));
            }
        }

        return matrix;
    }

    public static int getSessionId(String jsonString) {
        Map<String, Object> map = parseJSON(jsonString);
        return (int) map.get("sessionId");
    }
    public static int getHostId(String jsonString) {
        Map<String, Object> map = parseJSON(jsonString);
        return (int) map.get("hostId");
    }
    public static int getGuestId(String jsonString) {
        Map<String, Object> map = parseJSON(jsonString);
        return (int) map.get("guestId");
    }
    public static int getUserId(String jsonString) {
        Map<String, Object> map = parseJSON(jsonString);
        return (int) map.get("userId");
    }
    public static Boolean getTurn(String jsonString) {
        Map<String, Object> map = parseJSON(jsonString);
        return (Boolean) map.get("isHostTurn");
    }
    public static String getUserName(String jsonString) {
        Map<String, Object> map = parseJSON(jsonString);
        return (String) map.get("name");
    }
    public static int getUserSymbol(String jsonString) {
        Map<String, Object> map = parseJSON(jsonString);
        return (int) map.get("symbol");
    }
    public static String getField(String jsonString) {
        Map<String, Object> map = parseJSON(jsonString);
        return (String) map.get("field");
    }
}

