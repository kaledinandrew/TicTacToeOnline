package org.TicTacToeCLI;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class JsonParser {
    public static Map<String, Object> parseJSON(String jsonString) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(jsonString, new TypeReference<HashMap<String,Object>>(){});
        return map;
    }

    public static ArrayList<ArrayList<Integer>> parseField(String stringList) throws Exception {
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
}

