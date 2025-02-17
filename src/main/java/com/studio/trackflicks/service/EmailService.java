package com.studio.trackflicks.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${spring.mail.from}")
    private String from;

    /**
     * Sends an email using a Thymeleaf template.
     *
     * @param to Recipient email address
     * @param subject Subject of the email
     * @param templateName Name of the Thymeleaf template
     * @param variables Variables to be used in the template
     */
    public void sendEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true);

            Locale currentLocale = LocaleContextHolder.getLocale();
            log.debug("Current Locale: {}", LocaleContextHolder.getLocale());

            // Set email details
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(from);
            var context = new Context(currentLocale);
            context.setVariables(variables);

            // Generate HTML content using Thymeleaf
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true); // true enables HTML

            mailSender.send(message);
            log.debug("Email has been sent to {}", to);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
