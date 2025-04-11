package com.library.exception;

import com.library.constants.ErrorCodes;
import com.library.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${app.debug:false}") // default is false if not set
    private boolean debugMode;
    
    // Handle @Valid annotation errors (request body validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error instanceof FieldError 
                ? ((FieldError) error).getField() 
                : error.getObjectName();
            errors.put(fieldName, error.getDefaultMessage());
        });

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.<Map<String, String>>error(
                "Validation failed", 
                HttpStatus.BAD_REQUEST.value()
            ).withData(errors));
    }

    // Handle @Validated annotation errors (method parameter validation)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolation(
            ConstraintViolationException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            errors.put(fieldName, violation.getMessage());
        });

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.<Map<String, String>>error(
                "Invalid parameters", 
                HttpStatus.BAD_REQUEST.value()
            ).withData(errors));
    }

    // EntityAlreadyExistsException handler
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyExists(EntityAlreadyExistsException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ApiResponse.error(ex.getMessage(), ErrorCodes.CONFLICT_ERROR));
    }

    // MessageException handler
    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomError(MessageException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ex.getMessage(), ErrorCodes.PROCCESSING_ERROR));
    }

    // Generic exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleAllExceptions(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(
                debugMode ? ex.getMessage() : "An unexpected error ocurred",
                ErrorCodes.GENERAL_ERROR
            ));
    }
}