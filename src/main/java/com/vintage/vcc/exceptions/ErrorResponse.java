package com.vintage.vcc.exceptions;

import lombok.Data;

import java.util.Date;
@Data
public class ErrorResponse {

    private final int status;
    private final String message;
    private final Date timestamp;

    // Constructor
    public ErrorResponse(int status, String message, Date timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
}