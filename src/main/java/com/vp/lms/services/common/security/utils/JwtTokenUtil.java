package com.vp.lms.services.common.security.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    private static Logger logger = LogManager.getLogger(JwtTokenUtil.class);

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return JwtTokenProvider.getInstance().getClaims(token);
    }

    public String generateTokenWithExp(UserDetails userDetails, long millis, Map<String, Object> claims) {
        return JwtTokenProvider.getInstance().createToken(userDetails.getUsername(), claims, millis);
    }


}
