package com.example.cloudfide_igor_barcikowski_task.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpectedException(
            Exception e,
            HttpServletRequest request) {


        ApiError apiError = new ApiError(
                e.getClass().getName(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ZonedDateTime.now(),
                request.getRequestURI(),
                null
        );

        log.error("Unexpected server error on {} {} {} {}", request.getMethod(), request.getRequestURI(), e.getMessage(), apiError.status());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }



    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> handleNoSuchElementException(
            NoSuchElementException e,
            HttpServletRequest request
    ) {

        ApiError apiError = new ApiError(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                ZonedDateTime.now(),
                request.getRequestURI(),
                null
        );

        log.warn("Element not found on {} {} {} {}", request.getMethod(), request.getRequestURI(), e.getMessage(), apiError.status());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(
            IllegalArgumentException e,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                ZonedDateTime.now(),
                request.getRequestURI(),
                null
        );

        log.warn("Illegal Argument Exception {} {} {} {}", request.getMethod(), request.getRequestURI(), e.getMessage(), apiError.status());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }


    @ExceptionHandler(ResourceDoesNotBelongException.class)
    public ResponseEntity<ApiError> handleResourceDoesNotBelongException(
            ResourceDoesNotBelongException e,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                ZonedDateTime.now(),
                request.getRequestURI(),
                null
        );

        log.warn("Resource does not belong exception {} {} {} {}", request.getMethod(), request.getRequestURI(), e.getMessage(), apiError.status());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}