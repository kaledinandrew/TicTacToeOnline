package com.TicTacToeBackend.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TicTacToeExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleException(Exception exception) {
        return handleExceptionInternal(exception, ExceptionBody.createException(exception.getMessage()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<Object> handleException(IllegalArgumentException exception) {
        return handleExceptionInternal(exception, ExceptionBody.createException(exception.getMessage()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, null);
    }

}
