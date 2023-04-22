package com.vp.lms.common.security;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class HashUtils {
    public static String hash(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean checkEncrypted(String candidate, String hash){
        return BCrypt.checkpw(candidate, hash);
    }
}
