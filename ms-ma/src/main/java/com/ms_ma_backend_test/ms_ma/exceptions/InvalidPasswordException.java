package com.ms_ma_backend_test.ms_ma.exceptions;

public class InvalidPasswordException extends RuntimeException{

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException() {
        super("The password typed is not valid");
    }

}
