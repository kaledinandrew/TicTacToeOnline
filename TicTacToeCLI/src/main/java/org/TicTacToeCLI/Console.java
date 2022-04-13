package org.TicTacToeCLI;

import static org.TicTacToeCLI.Variables.*;
import static org.TicTacToeCLI.JsonParser.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;

public class Console implements SessionObserver {

    // connect to port, port/users, ...
    // need to be disconnected later
    public HttpURLConnection connectToSession(String link) throws IOException {
        URL url = null;
        HttpURLConnection connection = null;

        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            System.out.println("Undefined URL given!");
            e.printStackTrace();
            return null;
        }

        connection = (HttpURLConnection) url.openConnection();

        int code = connection.getResponseCode();
        if (HttpURLConnection.HTTP_OK == code) {
            System.out.println("OK 200");
            return connection;
        } else {
            System.out.printf("Failed with %d\n", code);
            return null;
        }
    }

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
    public void postUpdate(HttpURLConnection connection, Map<String, Object> parameters) throws Exception{
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

        System.out.println(stringBuilder.toString());

        // try-catch
        isR.close();
        bfR.close();
        os.close();

    }

    // POST/PATCH
    public void updateField() throws Exception {} // ???

    // POST/PATCH
    public void placeSymbol(int x, int y) throws Exception {
        // check if x, y are valid

        Map<String, Object> parameters  = new HashMap<>();
        parameters.put("point", new int[] {x, y});
        // ...
        var connection = this.connectToSession(SERVER_LINK + "/field"); // exmpl
        this.postUpdate(connection, parameters);
    }

    // GET
    public void showField() throws Exception {
        var connection = this.connectToSession(SERVER_LINK + "/field"); // exmpl
        var output = this.getUpdate(connection);
        // ... -- parse string
        int n, m; // exmpl shape
        n = 3; m = 3;
        int[][] field = new int[m][n]; // exmpl

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // some king of visualization
                break;
            }
        }
    }

    // GET
    public String showResult() throws Exception {
        var connection = this.connectToSession(SERVER_LINK + "/field"); // exmpl
        var output = this.getUpdate(connection);
        // ... -- parse string
        String result = "";
        if (result != "") {
            System.out.println("You " + result + "!");
        }

        return result;
    }
}
