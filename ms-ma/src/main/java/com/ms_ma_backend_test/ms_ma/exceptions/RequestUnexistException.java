package com.ms_ma_backend_test.ms_ma.exceptions;

public class RequestUnexistException extends RuntimeException{

    public RequestUnexistException(String message) {
        super(message);
    }

    public RequestUnexistException() {
        super("Request unexist ");
    }

}
