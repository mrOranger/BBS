package com.edoardo.bbs.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ValidationException extends Exception {
    private String message;

    public ValidationException () {
        super();
        this.message = "Invalid data.";
    }

    public ValidationException (String message) {
        super(message);
        this.message = message;
    }
}
