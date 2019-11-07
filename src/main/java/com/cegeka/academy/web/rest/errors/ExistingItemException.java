package com.cegeka.academy.web.rest.errors;

public class ExistingItemException extends Exception {

    String message;

    @Override
    public String getMessage() {

        return message;
    }

    public ExistingItemException setMessage(String message) {

        this.message = message;

        return this;
    }
}
