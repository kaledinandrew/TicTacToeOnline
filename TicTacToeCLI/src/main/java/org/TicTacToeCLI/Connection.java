package org.TicTacToeCLI;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connection {
    public static HttpURLConnection connect(String link) {
        URL url = null;
        HttpURLConnection connection = null;

        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            System.out.println("Undefined URL given!");
            e.printStackTrace();
            return null;
        }

        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
            return null;
        }

        int code = 0;
        try {
            code = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (HttpURLConnection.HTTP_OK == code) {
            System.out.println("OK 200");
            return connection;
        } else {
            System.out.printf("Failed with %d\n", code);
            return null;
        }
    }
}
