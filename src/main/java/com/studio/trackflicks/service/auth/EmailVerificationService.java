package com.studio.trackflicks.service.auth;

import com.studio.trackflicks.model.EmailVerification;
import com.studio.trackflicks.model.User;
import com.studio.trackflicks.repository.EmailVerificationRepository;
import com.studio.trackflicks.service.EmailService;
import com.studio.trackflicks.utils.EmailMessageAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailService emailService;
    private final EmailVerificationRepository verificationRepository;
    private final EmailMessageAccessor emailMessageAccessor;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 8;
    private static final Long expiration_minutes = 10L;
    private static final String EMAIL_SUBJECT = "email.subject.verification";
    private static final String TEMPLATE_NAME = "VerificationEmail";

    public void createVerification(User user) {
        EmailVerification toVerify = EmailVerification.builder()
                .user(user)
                .code(generateRandomCode())
                .creationDate(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusMinutes(expiration_minutes))
                .build();

        verificationRepository.save(toVerify);

        Map<String, Object> variables = new HashMap<>();
        variables.put("confirmationCode", generateRandomCode());

        final String subject = emailMessageAccessor.getMessage(null, EMAIL_SUBJECT);
        try {
            emailService.sendEmail(user.getEmail(), subject, TEMPLATE_NAME, variables);
        } catch(Exception ex) {
            throw new RuntimeException("SMTP Server error");
        }
    }

    public User verify(String verificationCode) {
        EmailVerification verification = verificationRepository.findByCode(verificationCode)
                .orElseThrow();

        if (LocalDateTime.now().isAfter(verification.getExpireDate())) {
            throw new RuntimeException("Code already expired ask a new one");
        }
        verification.setVerifiedDate(LocalDateTime.now());
        verificationRepository.save(verification);

        return verification.getUser();
    }

    private String generateRandomCode(){
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }

}