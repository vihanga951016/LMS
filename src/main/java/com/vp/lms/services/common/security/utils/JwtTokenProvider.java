package com.vp.lms.services.common.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class JwtTokenProvider {

    private static final String secretKey = Base64.getEncoder()
            .encodeToString("asduiqhwasiubdiubfnoinfobsdivbuiqbicbjzxncninfqijd9wjdjpoasdojpasjdpqw-id-0ajsdjasjdoasiodjasudhasdiasd8uahsidh"
                    .getBytes());
    private static JwtTokenProvider instance;

    public static JwtTokenProvider getInstance() {
        if (instance == null) {
            instance = new JwtTokenProvider();
        }
        return instance;
    }

    private static final Logger logger = LogManager.getLogger(JwtTokenProvider.class);

    public String createToken(String username, Map<String, Object> claimMap, long validityInMillis) {

        Claims claims = Jwts.claims().setSubject(username);
        logger.info("Token generating for subject >> " + username + " | validity millis " + validityInMillis);
        if (claimMap != null) {
            for (String key : claimMap.keySet()) {
                claims.put(key, claimMap.get(key));
            }
        }

        return getToken(username, validityInMillis, claims);
    }

    private String getToken(String username, long validityInMillis, Claims claims) {

        Date validity = new Date( validityInMillis);
        logger.info("Token validate until " + validity);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer("lms")
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        logger.info("Token generated for username >> " + username);

        return token;
    }

    public Claims getClaims(String jwt) throws ExpiredJwtException {
        //This line will throw an exception if it is not a signed JWS (as expected)
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

}
