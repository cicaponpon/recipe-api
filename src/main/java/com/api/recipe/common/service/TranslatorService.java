package com.api.recipe.common.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TranslatorService {

    private final MessageSource messageSource;

    @Value("${locale.default.lang.key:en}")
    private String languageKey;

    public String process(String key, Locale locale) {
        String message = messageSource.getMessage(key, new Object[0], locale);
        return StringUtils.isBlank(message) ? key : message;
    }

    public String process(String key, Object[] args, Locale locale) {
        String message = messageSource.getMessage(key, args, locale);
        return StringUtils.isBlank(message) ? key : message;
    }

    public String process(String key) {
        return process(key, new Locale(languageKey));
    }

    public String process(String key, Object[] args) {
        return process(key, args, new Locale(languageKey));
    }
}
