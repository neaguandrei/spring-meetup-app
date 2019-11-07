package com.cegeka.academy.web.rest.errors;

public class UnauthorizedUserException extends RuntimeException {

    String message;

    @Override
    public String getMessage() {
        return message;
    }

    public UnauthorizedUserException setMessage(String message) {
        this.message = message;
        return this;
    }
}
