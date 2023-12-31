package com.edoardo.bbs.exceptions;

public class MaximumAddressNumberException extends Exception {

    private String message;

    public MaximumAddressNumberException () {
        super("Maximum number of addresses.");
    }

    public MaximumAddressNumberException (String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage () {
        return this.message;
    }
}
