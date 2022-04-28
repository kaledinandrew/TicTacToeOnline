package com.TicTacToeBackend.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExceptionBody {

    private String message;

    @Override
    public String toString() {
        return "{\n\t\"message\": \"" + message + "\"\n}";
    }

    public static String createException(String message) {
        ExceptionBody exceptionBody = new ExceptionBody(message);
        return exceptionBody.toString();
    }
}
