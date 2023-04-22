package com.vp.lms.services.common.locale;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

public class LocaleManager {

    private static LocaleManager instance;
    private static ResourceBundleMessageSource messageSource;

    public static LocaleManager getInstance() {
        if (instance == null) {
            instance = new LocaleManager();

            messageSource = new ResourceBundleMessageSource();
            messageSource.setBasenames("locale/lang");
        }
        return instance;
    }

    public String getValue(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }
}
