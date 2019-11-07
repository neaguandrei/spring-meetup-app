package com.cegeka.academy.web.rest.errors;

public class InvalidInvitationStatusException extends Exception {

    String message;

    @Override
    public String getMessage() {
        return message;
    }

    public InvalidInvitationStatusException setMessage(String message) {

        this.message = message;

        return this;
    }
}
