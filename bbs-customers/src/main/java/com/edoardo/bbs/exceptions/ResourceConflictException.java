package com.edoardo.bbs.exceptions;

public class ResourceConflictException extends Exception {

    private String message;

    public ResourceConflictException () {
        super("Conflict.");
    }

    public ResourceConflictException (String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage () {
        return this.message;
    }
}
