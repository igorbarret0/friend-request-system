package com.ms_ma_backend_test.ms_ma.exceptions;

public class RequestAlreadyBeenAnsweredException extends RuntimeException {

    public RequestAlreadyBeenAnsweredException(String message) {
        super(message);
    }

    public RequestAlreadyBeenAnsweredException() {
        super("This request has already been answered");
    }

}
