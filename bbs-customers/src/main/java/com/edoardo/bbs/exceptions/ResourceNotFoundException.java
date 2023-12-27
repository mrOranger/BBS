package com.edoardo.bbs.exceptions;

public class ResourceNotFoundException extends Exception{

    private String message;

    public ResourceNotFoundException () {
        super();
        this.message = "Not found.";
    }

    public ResourceNotFoundException (String message) {
        super(message);
        this.message = message;
    }
}
