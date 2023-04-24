package com.vp.lms.exceptions;

public class AuthorizationException extends LMSExceptions {
    public AuthorizationException(String message, Exception ex) {
        super(message, ex);
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(Exception ex) {
        super(ex);
    }
}
