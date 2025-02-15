package com.studio.trackflicks.utils;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Objects;

public class GeneralMessageAccessor {

    private final MessageSource messageSource;

    GeneralMessageAccessor(@Qualifier("generalMessageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(Locale locale, String key, Object... parameter) {

        if (Objects.isNull(locale)) {
            return messageSource.getMessage(key, parameter, ProjectConstants.ENGLISH_LOCALE);
        }

        return messageSource.getMessage(key, parameter, locale);
    }

}
