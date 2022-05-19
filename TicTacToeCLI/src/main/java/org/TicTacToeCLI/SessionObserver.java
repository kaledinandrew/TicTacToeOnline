package org.TicTacToeCLI;

import java.net.HttpURLConnection;

public interface SessionObserver {
    public String getUpdate(HttpURLConnection connection);
    public String postUpdate(HttpURLConnection connection, String jsonInputString);
    public String putUpdate(HttpURLConnection connection, String jsonInputString);
}