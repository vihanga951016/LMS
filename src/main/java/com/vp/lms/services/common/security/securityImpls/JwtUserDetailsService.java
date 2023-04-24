package com.vp.lms.services.common.security.securityImpls;

import com.vp.lms.common.ApplicationConstant;
import com.vp.lms.common.enums.JwtTypes;
import com.vp.lms.common.http.locale.LocaleService;
import com.vp.lms.exceptions.AuthorizationException;
import com.vp.lms.services.common.security.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private static Logger logger = LogManager.getLogger(JwtUserDetailsService.class);

    private final JwtTokenUtil tokenUtil;
    private final LocaleService localeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(Arrays.stream(JwtTypes.values()).anyMatch(jwtTypes -> username.equals(jwtTypes.name()))) {
            return new User(username, ApplicationConstant.JWT_PW, new ArrayList<>());
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    public Claims authenticate(HttpServletRequest request, JwtTypes... jwtTypes) throws AuthorizationException {
        logger.info(request.getRequestURI() + " | Access Rights | " + Arrays.toString(jwtTypes));
        String requestTokenHeader = request.getHeader(ApplicationConstant.AUTH_HEADER);

        String jwtToken = null;

        if(requestTokenHeader != null &&
                requestTokenHeader.startsWith(ApplicationConstant.AUTH_HEADER_PREFIX)) {
            jwtToken = requestTokenHeader.substring(ApplicationConstant.AUTH_HEADER_PREFIX.length());
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if(tokenUtil.validateToken(jwtToken, jwtTypes)){
            return tokenUtil.getAllClaimsFromToken(jwtToken);
        }

        throw new AuthorizationException(localeService.getMessage("auth.forbidden", request));
    }
}
