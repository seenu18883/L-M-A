package com.library.app.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorDetails {


    private LocalDateTime timestamp;
    private String message;
    private String path;
    private String errorCode;

}
