package com.studio.trackflicks.exception.validator;

import com.studio.trackflicks.exception.ApiExceptionResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Order(1)
@RestControllerAdvice
public class ValidatorExceptionHandler {

    @ExceptionHandler(InvalidEmailException.class)
    ResponseEntity<ApiExceptionResponse> handleInvalidEmailException(InvalidEmailException exception) {
        final ApiExceptionResponse response = new ApiExceptionResponse(exception.getErrorMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    ResponseEntity<ApiExceptionResponse> handleInvalidPasswordException(InvalidPasswordException exception) {
        final ApiExceptionResponse response = new ApiExceptionResponse(exception.getErrorMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(InvalidUsernameException.class)
    ResponseEntity<ApiExceptionResponse> handleInvalidUsernameException(InvalidUsernameException exception) {
        final ApiExceptionResponse response = new ApiExceptionResponse(exception.getErrorMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
