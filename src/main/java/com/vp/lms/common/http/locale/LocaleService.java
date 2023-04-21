package com.vp.lms.common.http.locale;

import javax.servlet.http.HttpServletRequest;

public interface LocaleService {
    String getMessage(String code, HttpServletRequest request);
}
