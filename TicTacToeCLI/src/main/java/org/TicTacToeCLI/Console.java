package org.TicTacToeCLI;

import static org.TicTacToeCLI.Variables.*;
import static org.TicTacToeCLI.JsonParser.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Console implements SessionObserver {

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

        return stringBuilder.toString();
    }

    public String startSession(String hostId) throws Exception {
        var connection = Connection.connect(SESSIONS_LINK + "/?hostId=" + hostId);

        Map<String, Object> parameters  = new HashMap<>();
        // empty POST
        String response = this.postUpdate(connection, parameters);
        return response;
    }

    // need to be disconnected later
    public HttpURLConnection connectToSession(String sessionId, String guestId) throws Exception {
        var connection = Connection.connect(SESSIONS_CONNECT_LINK +
                "/?sessionId=" + sessionId + "&guestId=" + guestId);

        Map<String, Object> parameters  = new HashMap<>();

        // empty POST
        String response = this.postUpdate(connection, parameters);
        System.out.println(response);
        return connection;
    }

    // POST/PATCH
    public void updateField() throws Exception {} // ???

    // POST/PATCH
    public void placeSymbol(String sessionId, String userId, Integer x, Integer y) throws Exception {
        // check if x, y are valid
        var connection = Connection.connect(SERVER_LINK +
                "/?sessionId=" + sessionId + "&userId=" + userId +
                "&x=" + x.toString() + "&y=" + y.toString());

        // empty POST
        Map<String, Object> parameters  = new HashMap<>();
        String response = this.postUpdate(connection, parameters);
        System.out.println(response);
    }

    // GET
    public int showField(HttpURLConnection connection) throws Exception {
        String output = this.getUpdate(connection);

        Map<String, Object> mp = JsonParser.parseJSON(output);
        String mat = mp.get("field").toString();
        ArrayList<ArrayList<Integer>> matrix = JsonParser.parseField(mat);
        int m = matrix.size();
        int n = matrix.get(0).size();

        // visualize field
        {
            for (int i = 0; i < n; i++) {
                System.out.printf("\t%d", i);
            }

            System.out.println();

            for (int i = 0; i < m; i++) {
                for (int j = -1; j < n; j++) {
                    if (j == -1) {
                        System.out.printf("%d", i);
                    } else {
                        System.out.printf("\t%d", matrix.get(i).get(j));
                    }
                }
                System.out.println();
            }
        }

        // to void
        return m * n;
    }

    // GET
    public String showResult() throws Exception {
        var connection = Connection.connect(SERVER_LINK + "/field"); // exmpl
        var output = this.getUpdate(connection);
        // ... -- parse string
        String result = "";
        if (result != "") {
            System.out.println("You " + result + "!");
        }

        return result;
    }
}
