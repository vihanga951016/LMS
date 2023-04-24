package com.vp.lms.exceptions;

public class LMSExceptions extends Exception {

    public LMSExceptions(){super();}

    public LMSExceptions(Exception ex) {
        super(ex);
    }

    public LMSExceptions(String message) {
        super(message);
    }

    public LMSExceptions(String message, Exception ex) {
        super(message, ex);
    }
}
