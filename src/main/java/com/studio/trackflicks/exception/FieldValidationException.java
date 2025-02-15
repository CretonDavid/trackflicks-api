package com.studio.trackflicks.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldValidationException extends RuntimeException {

    private final String errorMessage;

}
