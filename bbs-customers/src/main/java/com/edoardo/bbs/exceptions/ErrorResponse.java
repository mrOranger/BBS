package com.edoardo.bbs.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private LocalDateTime date;
    private String message;

    public ErrorResponse (String message) {
        this.message = message;
        this.date = LocalDateTime.now();
    }
}
