package com.cegeka.academy.web.rest.errors;

public class InvalidSortingParamException extends RuntimeException {
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public InvalidSortingParamException setMessage(String message) {
        this.message = message;
        return this;
    }
}
