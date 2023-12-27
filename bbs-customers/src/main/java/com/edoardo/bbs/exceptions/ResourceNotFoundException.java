package com.edoardo.bbs.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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
