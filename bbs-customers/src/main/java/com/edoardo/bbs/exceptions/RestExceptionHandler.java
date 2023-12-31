package com.edoardo.bbs.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ErrorResponse> resourceNotFoundExceptionHandler (ResourceNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public final ResponseEntity<ErrorResponse> resourceConflictExceptionHandler (ResourceConflictException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleHandlerMethodValidationException(MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaximumAddressNumberException.class)
    protected ResponseEntity<ErrorResponse> maximumNumberOfAddressException (MaximumAddressNumberException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
