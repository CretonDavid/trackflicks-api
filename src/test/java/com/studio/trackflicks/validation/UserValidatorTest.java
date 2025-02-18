package com.studio.trackflicks.validation;

import com.studio.trackflicks.exception.validation.InvalidEmailException;
import com.studio.trackflicks.exception.validation.InvalidPasswordException;
import com.studio.trackflicks.exception.validation.InvalidUsernameException;
import com.studio.trackflicks.service.auth.JwtService;
import com.studio.trackflicks.utils.ExceptionMessageAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @InjectMocks
    private UserValidator userValidator;

    @Mock
    private ExceptionMessageAccessor exceptionMessageAccessor;

    @Test
    void testValidUsername(){
        assertDoesNotThrow(() -> userValidator.validateUsernamePattern("Username"));
        assertDoesNotThrow(() -> userValidator.validateUsernamePattern("User-name"));
        assertDoesNotThrow(() -> userValidator.validateUsernamePattern("1Username123"));
    }

    @Test
    void testInvalidUsername(){
        assertThrows(InvalidUsernameException.class, () -> userValidator.validateUsernamePattern("#Usern@me123!"));
        assertThrows(InvalidUsernameException.class, () -> userValidator.validateUsernamePattern("_Username"));
        assertThrows(InvalidUsernameException.class, () -> userValidator.validateUsernamePattern("User.name"));
    }

    @Test
    void testTooShortUsername(){
        assertThrows(InvalidUsernameException.class, () -> userValidator.validateUsernamePattern("US"));
    }

    @Test
    void testTooLongUsername(){
        assertThrows(InvalidUsernameException.class, () -> userValidator.validateUsernamePattern("azertyuiopqsdfghjklmwxcvbn"));
    }

    @Test
    void testValidEmail(){
        assertDoesNotThrow(() -> userValidator.validateEmailPattern("user@example.com"));
        assertDoesNotThrow(() -> userValidator.validateEmailPattern("user123@example.com"));
        assertDoesNotThrow(() -> userValidator.validateEmailPattern("first.last@example.com"));
        assertDoesNotThrow(() -> userValidator.validateEmailPattern("user+alias@example.com"));
        assertDoesNotThrow(() -> userValidator.validateEmailPattern("user@mail.example.com"));
        assertDoesNotThrow(() -> userValidator.validateEmailPattern("user@example.museum"));
        assertDoesNotThrow(() -> userValidator.validateEmailPattern("user_name@example.com"));
    }

    @Test
    void testInvalidEmail(){
        // Missing local part
        assertThrows(InvalidEmailException.class, () -> userValidator.validateEmailPattern("@domain.com"));
        // No domain name
        assertThrows(InvalidEmailException.class, () -> userValidator.validateEmailPattern("user@.com"));
        // No TLD
        assertThrows(InvalidEmailException.class, () -> userValidator.validateEmailPattern("user@domain"));
        // Missing @
        assertThrows(InvalidEmailException.class, () -> userValidator.validateEmailPattern("userdomain.com"));
        // Multiple @
        assertThrows(InvalidEmailException.class, () -> userValidator.validateEmailPattern("user@@domain.com"));
        // Invalid character
        assertThrows(InvalidEmailException.class, () -> userValidator.validateEmailPattern("user#name@domain.com"));
        // Space
        assertThrows(InvalidEmailException.class, () -> userValidator.validateEmailPattern("user name@domain.com"));
        // Double dot before TLD
        assertThrows(InvalidEmailException.class, () -> userValidator.validateEmailPattern("user@domain..com"));
        // Double dot in local part
        assertThrows(InvalidEmailException.class, () -> userValidator.validateEmailPattern("user..name@domain.com"));
    }

    @Test
    void testValidPassword() {
        assertDoesNotThrow(() -> userValidator.validatePasswordPattern("ThisP@ssw0rd1sStrongEn0ugh"));
    }

    @Test
    void testTooShortPassword() {
        assertThrows(InvalidPasswordException.class, () -> userValidator.validatePasswordPattern("Sh0rt"));
    }

    @Test
    void testMissingUppercasePassword() {
        assertThrows(InvalidPasswordException.class, () -> userValidator.validatePasswordPattern("weakp@ssw0rd"));
    }

    @Test
    void testMissingLowercasePassword() {
        assertThrows(InvalidPasswordException.class, () -> userValidator.validatePasswordPattern("WEAKP@SSW0RD"));
    }

    @Test
    void testMissingSpecialCharacterPassword() {
        assertThrows(InvalidPasswordException.class, () -> userValidator.validatePasswordPattern("Weakpassw0rd"));
    }

    @Test
    void testMissingNumberPassword() {
        assertThrows(InvalidPasswordException.class, () -> userValidator.validatePasswordPattern("Weakp@ssword"));
    }

}