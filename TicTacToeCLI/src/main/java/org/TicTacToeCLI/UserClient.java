package org.TicTacToeCLI;

import org.TicTacToeCLI.*;
import static org.TicTacToeCLI.Variables.*;
import static org.TicTacToeCLI.Connection.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class UserClient implements SessionObserver {

    @Override // suppose connection code == 200
    public String getUpdate(HttpURLConnection connection) throws Exception {
        // check if connection is valid
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(CONNECTION_TIMEOUT);

        var reader = new InputStreamReader(connection.getInputStream());
        try (BufferedReader in = new BufferedReader(reader)) {
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    // https://www.youtube.com/watch?v=pc_jrANrjUc&list=PL81zTpL449O1KU5CCjGGqLXoxqZQj6pNr&index=11
    @Override // suppose connection code == 200
    public String postUpdate(HttpURLConnection connection, Map<String, Object> parameters) throws Exception{
        // check if connection is valid
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);

//      connection.setRequestProperty("User-Agent", "Ilon Musk");
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(CONNECTION_TIMEOUT);

        OutputStream os = null; // отправлять
        InputStreamReader isR = null; // принимать
        BufferedReader bfR = null;
        StringBuilder stringBuilder = new StringBuilder();

        byte[] out = parameters.toString().getBytes();

        try {
            os = connection.getOutputStream();
            os.write(out);
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }

        isR = new InputStreamReader(connection.getInputStream());
        bfR = new BufferedReader(isR);
        String line;
        while ((line = bfR.readLine()) != null) {
            stringBuilder.append(line);
        }

        // try-catch
        isR.close();
        bfR.close();
        os.close();

        // if all's good
        return stringBuilder.toString();
    }

    // POST
    public String createUser(String name, int symbol) throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("symbol", symbol);

        var connection = connect(USERS_LINK);

        if (Objects.nonNull(connection)) {
            return this.postUpdate(connection, parameters);
        } else {
            return "";
        }
    }
    // POST/PATCH
    public void updateUser() {}
    // DELETE
    public void deleteUser() {}
}
