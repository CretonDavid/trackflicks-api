package com.studio.trackflicks.exception.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmailAlreadyExist extends RuntimeException {

    private final String errorMessage;

}
