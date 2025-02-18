package com.studio.trackflicks.exception.validator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidPasswordException extends RuntimeException {

    private final String errorMessage;

}
