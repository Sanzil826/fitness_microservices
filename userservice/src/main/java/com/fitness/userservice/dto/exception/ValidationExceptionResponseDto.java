package com.fitness.userservice.dto.exception;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ValidationExceptionResponseDto {

    private LocalDateTime localDateTime;
    private Integer statusCode;
    private String error;
    private String message;
    private String path;
    private Map<String, String> fieldErrors;

    public ValidationExceptionResponseDto(LocalDateTime localDateTime, Integer statusCode, String error, String message, String path, Map<String, String> fieldErrors) {
        this.localDateTime = localDateTime;
        this.statusCode = statusCode;
        this.error = error;
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }
}
