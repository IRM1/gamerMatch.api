package com.mew.demo.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private ResponseEntity<Map<String, Object>> buildErrorResponse(
      Exception ex, HttpStatus status, String error, HttpServletRequest request) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", java.time.LocalDateTime.now());
    body.put("status", status.value());
    body.put("error", error);
    body.put("message", ex.getMessage());
    body.put("path", request.getRequestURI());
    return new ResponseEntity<>(body, status);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(
      EntityNotFoundException ex, HttpServletRequest request) {
    return buildErrorResponse(ex, HttpStatus.NOT_FOUND, "Not Found", request);
  }

  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<Map<String, Object>> handleEntityExistsException(
      EntityExistsException ex, HttpServletRequest request) {
    return buildErrorResponse(ex, HttpStatus.CONFLICT, "Conflict", request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationErrors(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", java.time.LocalDateTime.now());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Validation Failed");
    body.put("message", errors);
    body.put("path", request.getRequestURI());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgument(
      IllegalArgumentException ex, HttpServletRequest request) {
    return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, "Bad Request", request);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<Map<String, Object>> handleAuthenticationException(
      AuthenticationException ex, HttpServletRequest request) {
    return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, "Unauthorized", request);
  }

  @ExceptionHandler(ForbiddenOperationException.class)
  public ResponseEntity<Map<String, Object>> handleForbiddenOperation(
      ForbiddenOperationException ex, HttpServletRequest request) {
    return buildErrorResponse(ex, HttpStatus.FORBIDDEN, "Forbidden", request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleAllOtherExceptions(
      Exception ex, HttpServletRequest request) {
    return buildErrorResponse(
        ex, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", request);
  }
}
