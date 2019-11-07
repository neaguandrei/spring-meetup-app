package com.cegeka.academy.web.rest.errors;

public class InvalidUserChallengeStatusException extends Exception {

    String message;

    @Override
    public String getMessage() {
        return message;
    }

    public InvalidUserChallengeStatusException setMessage(String message) {

        this.message = message;

        return this;
    }
}
