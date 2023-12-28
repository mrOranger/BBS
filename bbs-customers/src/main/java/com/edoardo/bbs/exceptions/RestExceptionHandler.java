package com.edoardo.bbs.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ErrorResponse> resourceNotFoundExceptionHandler (ResourceNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public final ResponseEntity<ErrorResponse> resourceConflictExceptionHandler (ResourceConflictException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<ErrorResponse> validationExceptionHandler (ValidationException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
