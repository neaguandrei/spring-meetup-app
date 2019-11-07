package com.cegeka.academy.web.rest;

import com.cegeka.academy.web.rest.errors.InvalidInvitationStatusException;
import com.cegeka.academy.web.rest.errors.InvalidUserChallengeStatusException;
import com.cegeka.academy.web.rest.errors.InvalidFieldException;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException exception)
    {
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List errors = new ArrayList();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });

        return new ResponseEntity<>(errors.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<String> handleInvalidFieldException(InvalidFieldException exception) {
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidInvitationStatusException.class)
    public ResponseEntity<String> handleInvalidInvitationStatusException(InvalidInvitationStatusException exception) {

        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = InvalidUserChallengeStatusException.class)
    public ResponseEntity<String> handleInvalidUserChallengeStatusException(InvalidUserChallengeStatusException exception) {

        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);

    }


}
