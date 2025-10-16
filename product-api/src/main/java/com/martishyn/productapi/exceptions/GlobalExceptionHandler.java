package com.martishyn.productapi.exceptions;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentValidation(MethodArgumentNotValidException exception) {
        Map<String, Object> errors = new HashMap<>();
        List<Map<String, String>> listOfErrors = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> Map.of(error.getField(), error.getDefaultMessage()))
                .toList();
        errors.put("errors", listOfErrors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationValidation(ConstraintViolationException exception) {
        Map<String, Object> errors = new HashMap<>();
        List<Map<Path, String>> listOfErrors = exception.getConstraintViolations()
                .stream()
                .map(error -> Map.of(error.getPropertyPath(), error.getMessage()))
                .toList();
        errors.put("errors", listOfErrors);
        return ResponseEntity.badRequest().body(errors);
    }
}
