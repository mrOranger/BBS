package com.edoardo.bbs.exceptions;

public class ResourceNotFoundException extends Exception{

    private String message;

    public ResourceNotFoundException () {
        super("Not found.");
    }

    public ResourceNotFoundException (String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage () {
        return this.message;
    }
}
