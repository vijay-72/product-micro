package com.example.productservice.exception;


import org.springframework.http.HttpStatus;

public class GeneralInternalException extends RuntimeException {
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    private HttpStatus httpStatus;
    public GeneralInternalException(String message, HttpStatus status) {
        super(message);
        httpStatus = status;
    }

    public GeneralInternalException(String message) {
        super(message);
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
