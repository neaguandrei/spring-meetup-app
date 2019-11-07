package com.cegeka.academy.web.rest.errors.controllerException;

import java.util.List;

public class ErrorResponse {

    private String message;
    private int code;
    private List<String> details;

    public ErrorResponse(String message, int code, List<String> details) {
        this.message = message;
        this.code = code;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", details=" + details + '}';
    }
}
