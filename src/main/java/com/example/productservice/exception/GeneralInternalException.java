package com.example.productservice.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GeneralInternalException extends RuntimeException {

    private final HttpStatus httpStatus;
    public GeneralInternalException(String message, HttpStatus status) {
        super(message);
        httpStatus = status;
    }

    public GeneralInternalException(String message) {
        super(message);
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
