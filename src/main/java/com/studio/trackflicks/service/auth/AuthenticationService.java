package com.studio.trackflicks.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studio.trackflicks.dto.auth.AuthenticationResponse;
import com.studio.trackflicks.dto.auth.LoginRequest;
import com.studio.trackflicks.dto.auth.RegisterRequest;
import com.studio.trackflicks.exception.auth.EmailAlreadyExist;
import com.studio.trackflicks.exception.auth.UserNotFoundException;
import com.studio.trackflicks.mapper.UserMapper;
import com.studio.trackflicks.model.Role;
import com.studio.trackflicks.model.Token;
import com.studio.trackflicks.model.TokenType;
import com.studio.trackflicks.model.User;
import com.studio.trackflicks.repository.TokenRepository;
import com.studio.trackflicks.repository.UserRepository;
import com.studio.trackflicks.utils.ExceptionMessageAccessor;
import com.studio.trackflicks.validation.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String EMAIL_ALREADY_EXISTS = "email.already.exist";

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserValidator userValidator;
    private final EmailVerificationService emailVerificationService;
    private final ExceptionMessageAccessor exceptionMessageAccessor;

    public void register(RegisterRequest request) {

        userValidator.validateEmailPattern(request.getEmail());
        final boolean existsByEmail = userRepository.existsByEmail(request.getEmail());
        if (existsByEmail) {
            log.warn("{} is already being used!", request.getEmail());

            final String existsEmail = exceptionMessageAccessor.getMessage(null, EMAIL_ALREADY_EXISTS);
            throw new EmailAlreadyExist(existsEmail);
        }
        userValidator.validateUsernamePattern(request.getUsername());
        userValidator.validatePasswordPattern(request.getPassword());

        final User user = UserMapper.INSTANCE.convertToUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        log.debug("A new user has been created with email : {}", user.getEmail());

        emailVerificationService.createVerification(savedUser);
    }

    public void verifyEmail(String verificationCode, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (user.isLocked()) {
            user = emailVerificationService.verify(verificationCode);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("Email already verified");
        }
    }


    public AuthenticationResponse login(LoginRequest request) {

        userValidator.validateEmailPattern(request.getEmail());

        final var authenticationTokenToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        authenticationManager.authenticate(authenticationTokenToken);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        log.debug("{} has successfully logged in!", user.getUsername());
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}
