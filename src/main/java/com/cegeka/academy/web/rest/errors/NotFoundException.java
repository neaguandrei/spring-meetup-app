package com.cegeka.academy.web.rest.errors;

public class NotFoundException extends Exception {

    String message;

    @Override
    public String getMessage() {
        return message;
    }

    public NotFoundException setMessage(String message) {
        this.message = message;
        return this;
    }
}
