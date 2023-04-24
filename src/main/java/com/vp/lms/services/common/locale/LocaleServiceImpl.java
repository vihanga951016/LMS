package com.vp.lms.services.common.locale;

import com.vp.lms.common.http.locale.LocaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class LocaleServiceImpl implements LocaleService {

    private final LocaleResolver localeResolver;

    @Override
    public String getMessage(String code, HttpServletRequest request) {
        return LocaleManager.getInstance().getValue(code, localeResolver.resolveLocale(request));
    }
}
