package com.cegeka.academy.web.rest.errors;

public class InvalidFieldException extends RuntimeException {

    String message;

    @Override
    public String getMessage() {
        return message;
    }

    public InvalidFieldException setMessage(String message) {
        this.message = message;
        return this;
    }
}
