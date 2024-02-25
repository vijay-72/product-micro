package com.example.productservice.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class BaseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Throwable.class})
    public ProblemDetail handleRuntimeException(Throwable exception) {
        logger.error("Unexpected exception", exception);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
        problemDetail.setTitle("Something went wrong, please try again latter");
        problemDetail.setType(URI.create("http://localhost:8080/errors/internalServerError"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler({GeneralInternalException.class})
    public ProblemDetail handleGeneralInternalException(GeneralInternalException exception) {
        HttpStatus status = exception.getHttpStatus();
        logger.error(exception.getMessage(), exception);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
        problemDetail.setTitle(getTitle(status));
        problemDetail.setType(URI.create(getUri(status)));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        // Create the list of invalid parameters
        List<Map<String, String>> invalidParams = fieldErrors.stream()
                .map(fieldError -> {
                    Map<String, String> invalidParam = new LinkedHashMap<>();
                    invalidParam.put("field", fieldError.getField());
                    invalidParam.put("reason", fieldError.getDefaultMessage());
                    return invalidParam;
                })
                .collect(Collectors.toList());

        // Create the ProblemDetail object
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, "Please correct invalid params shown below");
        problemDetail.setTitle("Your request parameters didn't validate.");
        problemDetail.setType(URI.create("http://localhost:8080/errors/invalidParameters"));
        problemDetail.setProperty("invalid-params", invalidParams);
        problemDetail.setProperty("timestamp", Instant.now());

        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(
            ConstraintViolationException ex) {
        log.error(ex.getMessage(), ex);
        List<Map<String, String>> invalidVariables = ex.getConstraintViolations().stream()
                .map(violation -> Map.of(
                        "field", violation.getPropertyPath().toString(),
                        "reason", violation.getMessage()))
                .collect(Collectors.toList());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Please correct invalid path/query params shown below");
        problemDetail.setTitle("Invalid path/query params in the request.");
        problemDetail.setType(URI.create("http://localhost:8080/errors/badRequest"));
        problemDetail.setProperty("invalid-variables", invalidVariables);
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }


    public String getUri(HttpStatus status) {
        return switch (status) {
            case INTERNAL_SERVER_ERROR -> "http://localhost:8080/errors/internalServerError";
            case BAD_REQUEST -> "http://localhost:8080/errors/badRequest";
            case NOT_FOUND -> "http://localhost:8080/errors/notFound";
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
    }

    public String getTitle(HttpStatus status) {
        return switch (status) {
            case INTERNAL_SERVER_ERROR -> "Internal server error";
            case BAD_REQUEST -> "Invalid request";
            case NOT_FOUND -> "Not found";
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
    }

}
