package com.studio.trackflicks.exception.validator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidUsernameException extends RuntimeException {

    private final String errorMessage;

}
