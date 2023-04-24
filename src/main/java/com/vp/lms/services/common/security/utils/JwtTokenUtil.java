package com.vp.lms.services.common.security.utils;

import com.vp.lms.common.enums.JwtTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
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

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token, JwtTypes[] jwtTypes) {
        try {
            final String username = getUsernameFromToken(token);
            if ("SUPER_ADMIN".equals(username)) return !isTokenExpired(token);
            else if (Arrays.stream(jwtTypes).anyMatch(types -> username.equals(types.name()))) {
                return !isTokenExpired(token);
            }
        } catch (Exception e) {
            logger.error("Error Validating Token !", e);
        }
        return false;
    }

}
