package com.vp.lms.services.common.locale;

import com.vp.lms.common.http.locale.LocaleService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;

@Service
public class LocaleServiceImpl implements LocaleService {

    private LocaleResolver localeResolver;

    @Override
    public String getMessage(String code, HttpServletRequest request) {
        return LocaleManager.getInstance().getValue(code, localeResolver.resolveLocale(request));
    }
}
