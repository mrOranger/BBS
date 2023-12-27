package com.edoardo.bbs.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ErrorResponse {
    private LocalDateTime date;
    private String message;

    public ErrorResponse (String message) {
        this.message = message;
        this.date = LocalDateTime.now();
    }
}
