package com.studio.trackflicks.validation;

import com.studio.trackflicks.exception.validator.InvalidEmailException;
import com.studio.trackflicks.exception.validator.InvalidPasswordException;
import com.studio.trackflicks.exception.validator.InvalidUsernameException;
import com.studio.trackflicks.utils.ExceptionMessageAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator {

    private static final String USERNAME_REGEX = "^[A-Za-zÀ-ÿ0-9][A-Za-zÀ-ÿ0-9'\\- ]*$";
    private static final Pattern USERNAME_PATTERN = Pattern.compile(USERNAME_REGEX);
    private static final String EMAIL_REGEX = "(?!.*\\.\\.)([A-Za-z0-9._%+-]+)@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    public static final String UPPERCASE_REGEX = ".*[A-Z].*";
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(UPPERCASE_REGEX);
    public static final String LOWERCASE_REGEX = ".*[a-z].*";
    public static final Pattern LOWERCASE_PATTERN = Pattern.compile(LOWERCASE_REGEX);
    public static final String NUMBER_REGEX = ".*[0-9].*";
    public static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_REGEX);
    public static final String SPECIAL_CHAR_REGEX = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*";
    public static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(SPECIAL_CHAR_REGEX);

    private static final String USERNAME_INVALID = "username.invalid";
    private static final String USERNAME_INVALID_SIZE = "username.invalid.size";
    private static final String EMAIL_INVALID = "email.invalid";
    private static final String PASS_INCORRECT_SIZE = "password.incorrect.size";
    private static final String PASS_MISSING_UPPER = "password.missing.uppercase";
    private static final String PASS_MISSING_LOWER = "password.missing.lowercase";
    private static final String PASS_MISSING_NB = "password.missing.number";
    private static final String PASS_MISSING_SPECIAL = "password.missing.special.char";

    private final ExceptionMessageAccessor exceptionMessageAccessor;

    public void validateUsernamePattern(String username) {

        if (username.length() < 3 || username.length() > 25) {
            final String invalidSizeUsername = exceptionMessageAccessor.getMessage(null, USERNAME_INVALID_SIZE);
            throw new InvalidUsernameException(invalidSizeUsername);
        }

        final boolean isValid = USERNAME_PATTERN.matcher(username).matches();
        if (!isValid) {
            log.warn("{} is a weird username!", username);

            final String invalidUsername = exceptionMessageAccessor.getMessage(null, USERNAME_INVALID);
            throw new InvalidUsernameException(invalidUsername);
        }
    }

    public void validateEmailPattern(String email) {

        final boolean isValid = EMAIL_PATTERN.matcher(email).matches();
        if (!isValid) {
            log.warn("{} is a weird email!", email);

            final String invalidEmail = exceptionMessageAccessor.getMessage(null, EMAIL_INVALID);
            throw new InvalidEmailException(invalidEmail);
        }
    }

    public void validatePasswordPattern(String password) {

        if (password.length() < 8 || password.length() > 72) {
            final String invalidPassword = exceptionMessageAccessor.getMessage(null, PASS_INCORRECT_SIZE);
            throw new InvalidPasswordException(invalidPassword);
        }

        if (!UPPERCASE_PATTERN.matcher(password).matches()) {
            final String invalidPassword = exceptionMessageAccessor.getMessage(null, PASS_MISSING_UPPER);
            throw new InvalidPasswordException(invalidPassword);
        }

        if (!LOWERCASE_PATTERN.matcher(password).matches()) {
            final String invalidPassword = exceptionMessageAccessor.getMessage(null, PASS_MISSING_LOWER);
            throw new InvalidPasswordException(invalidPassword);
        }

        if (!NUMBER_PATTERN.matcher(password).matches()) {
            final String invalidPassword = exceptionMessageAccessor.getMessage(null, PASS_MISSING_NB);
            throw new InvalidPasswordException(invalidPassword);
        }

        if (!SPECIAL_CHAR_PATTERN.matcher(password).matches()) {
            final String invalidPassword = exceptionMessageAccessor.getMessage(null, PASS_MISSING_SPECIAL);
            throw new InvalidPasswordException(invalidPassword);
        }
    }

}
