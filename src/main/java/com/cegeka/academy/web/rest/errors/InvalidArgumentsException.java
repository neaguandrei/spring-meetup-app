package com.cegeka.academy.web.rest.errors;

public class InvalidArgumentsException extends Exception {

    String message;

    @Override
    public String getMessage() {
        return message;
    }

    public InvalidArgumentsException setMessage(String message) {
        this.message = message;
        return this;
    }
}
