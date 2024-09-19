package com.ms_ma_backend_test.ms_ma.exceptions;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException() {
        super("This username has already been used");
    }

}
