package com.fitness.userservice.dto.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExceptionResponseDto {

    private LocalDateTime localDateTime;
    private Integer statusCode;
    private String error;
    private String message;
    private String path;

    public ExceptionResponseDto(LocalDateTime localDateTime, Integer statusCode, String error, String message, String path) {
        this.localDateTime = localDateTime;
        this.statusCode = statusCode;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
