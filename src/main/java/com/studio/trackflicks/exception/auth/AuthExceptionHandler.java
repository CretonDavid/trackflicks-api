package com.studio.trackflicks.exception.auth;

import com.studio.trackflicks.exception.ApiExceptionResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Order(2)
@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ApiExceptionResponse> handleUserNotFoundException(UserNotFoundException exception) {
        final ApiExceptionResponse response = new ApiExceptionResponse(exception.getErrorMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(InvalidTokenException.class)
    ResponseEntity<ApiExceptionResponse> handleInvalidTokenException(InvalidTokenException exception) {
        final ApiExceptionResponse response = new ApiExceptionResponse(exception.getErrorMessage(), HttpStatus.UNAUTHORIZED, LocalDateTime.now());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
