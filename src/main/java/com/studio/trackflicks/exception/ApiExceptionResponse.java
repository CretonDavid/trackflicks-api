package com.studio.trackflicks.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiExceptionResponse {

    private String message;

    private HttpStatus status;

    private LocalDateTime time;

}
