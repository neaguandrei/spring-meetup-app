package com.cegeka.academy.web.rest.errors.controllerException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

public abstract class HttpResponseExceptionHandler {

    protected ResponseEntity<ErrorResponse> getErrorResponseEntity(
            String message,
            int errorCode,
            List<String> details,
            HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(message, errorCode, details);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.set("Content-type", MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(errorResponse, headers, status);
    }
}
