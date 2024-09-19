package com.ms_ma_backend_test.ms_ma.exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        super("This user could not be found");
    }

}
