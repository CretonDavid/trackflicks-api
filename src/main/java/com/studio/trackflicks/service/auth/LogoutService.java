package com.studio.trackflicks.service.auth;

import com.studio.trackflicks.exception.auth.InvalidTokenException;
import com.studio.trackflicks.model.Token;
import com.studio.trackflicks.repository.TokenRepository;
import com.studio.trackflicks.utils.ExceptionMessageAccessor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private static final String TOKEN_ERROR = "token.error";
    private static final String TOKEN_ALREADY_EXPIRED = "token.already.expired";
    private final TokenRepository tokenRepository;
    private final ExceptionMessageAccessor exceptionMessageAccessor;

    /**
     * Handles the logout process by invalidating the JWT token associated with the user
     *
     * @param request test
     * @param response test
     * @param authentication test
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        // Extract JWT from Authorization Header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            // Missing or invalid Token
            final String MissingOrInvalidToken = exceptionMessageAccessor.getMessage(null, TOKEN_ERROR);
            throw new InvalidTokenException(MissingOrInvalidToken);
        }
        jwt = authHeader.substring(7);

        Token storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);

        // Mark Token as Expired
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
            log.debug("Logging out : Token is removed");
        } else {
            final String MissingOrInvalidToken = exceptionMessageAccessor.getMessage(null, TOKEN_ALREADY_EXPIRED);
            throw new InvalidTokenException(MissingOrInvalidToken);
        }
    }

}
