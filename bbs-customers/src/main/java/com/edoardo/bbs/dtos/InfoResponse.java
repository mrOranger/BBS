package com.edoardo.bbs.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InfoResponse {
    private String message;
    private LocalDateTime date;
}
