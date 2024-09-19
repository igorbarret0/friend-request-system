package com.ms_ma_backend_test.ms_ma.exceptions;

public class InviteYourselfException extends RuntimeException{

    public InviteYourselfException(String message) {
        super(message);
    }

    public InviteYourselfException() {
        super("It is not possible sent a friendship request for yourself");
    }

}
