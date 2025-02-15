package com.studio.trackflicks.config;

import com.studio.trackflicks.utils.ProjectConstants;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class MessageConfiguration {

    @Bean
    public MessageSource generalMessageSource() {

        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/i18n/general/GeneralMessages");
        messageSource.setDefaultEncoding(ProjectConstants.DEFAULT_ENCODING);

        return messageSource;
    }

    @Bean
    public MessageSource exceptionMessageSource() {

        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/i18n/exception/ExceptionMessages");
        messageSource.setDefaultEncoding(ProjectConstants.DEFAULT_ENCODING);

        return messageSource;
    }

    @Bean
    public MessageSource validationMessageSource() {

        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/i18n/validation/ValidationMessages");
        messageSource.setDefaultEncoding(ProjectConstants.DEFAULT_ENCODING);

        return messageSource;
    }

    @Bean
    public MessageSource emailMessageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/i18n/email/EmailMessages");
        messageSource.setDefaultEncoding(ProjectConstants.DEFAULT_ENCODING);
        messageSource.setFallbackToSystemLocale(false);

        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {

        final LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(validationMessageSource());

        return bean;
    }
}
