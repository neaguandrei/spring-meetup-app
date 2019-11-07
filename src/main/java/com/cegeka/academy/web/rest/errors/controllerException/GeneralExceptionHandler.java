package com.cegeka.academy.web.rest.errors.controllerException;

import com.cegeka.academy.service.invitation.InvitationServiceImpl;
import com.cegeka.academy.web.rest.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GeneralExceptionHandler extends HttpResponseExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(InvitationServiceImpl.class);


    @ExceptionHandler(value = ExpiredObjectException.class)
    public ResponseEntity<ErrorResponse> handleExpiredEvent(ExpiredObjectException e) {
        List<String> detailsList = new ArrayList<>();
        detailsList.add("ErrorMessage: " + e.getMessage());

        logger.info(e + "");

        return getErrorResponseEntity(
                ErrorCode.EVENT_EXPIRED.getMessage(),
                ErrorCode.EVENT_EXPIRED.getCode(),
                detailsList,
                HttpStatus.GONE);
    }

    @ExceptionHandler(value = UnauthorizedUserException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(UnauthorizedUserException e) {
        List<String> detailsList = new ArrayList<>();
        detailsList.add("ErrorMessage: " + e.getMessage());

        logger.info(e + "");

        return getErrorResponseEntity(
                ErrorCode.UNAUTHORIZED_ACCESS.getMessage(),
                ErrorCode.UNAUTHORIZED_ACCESS.getCode(),
                detailsList,
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        List<String> detailsList = new ArrayList<>();
        detailsList.add("ErrorMessage: " + e.getMessage());

        logger.info(e + "");

        return getErrorResponseEntity(
                ErrorCode.NOT_FOUND.getMessage(),
                ErrorCode.NOT_FOUND.getCode(),
                detailsList,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationFieldExceptions(MethodArgumentNotValidException ex) {
        List<String> detailsList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            detailsList.add("fieldName: " + fieldName);
            detailsList.add("errorMessage: " + errorMessage);

        });

        logger.info(ex + "");

        return getErrorResponseEntity(
                ErrorCode.INVALID_ARGUMENT.getMessage(),
                ErrorCode.INVALID_ARGUMENT.getCode(),
                detailsList,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidArgumentsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidArgumentsExceptions(InvalidArgumentsException e) {
        List<String> detailsList = new ArrayList<>();
        detailsList.add("ErrorMessage: " + e.getMessage());

        logger.info(e + "");

        return getErrorResponseEntity(
                ErrorCode.INVALID_ARGUMENT.getMessage(),
                ErrorCode.INVALID_ARGUMENT.getCode(),
                detailsList,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidFieldException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFieldException(InvalidFieldException e) {
        List<String> detailsList = new ArrayList<>();
        detailsList.add("ErrorMessage: " + e.getMessage());

        logger.info(e + "");

        return getErrorResponseEntity(
                ErrorCode.INVALID_ARGUMENT.getMessage(),
                ErrorCode.INVALID_ARGUMENT.getCode(),
                detailsList,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidInvitationStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInvitationStatusException(InvalidInvitationStatusException e) {
        List<String> detailsList = new ArrayList<>();
        detailsList.add("ErrorMessage: " + e.getMessage());

        logger.info(e + "");

        return getErrorResponseEntity(
                ErrorCode.INVALID_STATUS.getMessage(),
                ErrorCode.INVALID_STATUS.getCode(),
                detailsList,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidUserChallengeStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserChallengeStatusException(InvalidUserChallengeStatusException e) {
        List<String> detailsList = new ArrayList<>();
        detailsList.add("ErrorMessage: " + e.getMessage());

        logger.info(e + "");

        return getErrorResponseEntity(
                ErrorCode.INVALID_STATUS.getMessage(),
                ErrorCode.INVALID_STATUS.getCode(),
                detailsList,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ExistingItemException.class)
    public ResponseEntity<ErrorResponse> handleExistingItemException(ExistingItemException e) {
        List<String> detailsList = new ArrayList<>();
        detailsList.add("ErrorMessage: " + e.getMessage());

        logger.info(e + "");

        return getErrorResponseEntity(
                ErrorCode.EXISTING_ITEM.getMessage(),
                ErrorCode.EXISTING_ITEM.getCode(),
                detailsList,
                HttpStatus.CONFLICT);
    }
}
