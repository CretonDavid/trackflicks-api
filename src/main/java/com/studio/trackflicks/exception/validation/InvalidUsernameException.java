package com.studio.trackflicks.exception.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidUsernameException extends RuntimeException {

    private final String errorMessage;

}
