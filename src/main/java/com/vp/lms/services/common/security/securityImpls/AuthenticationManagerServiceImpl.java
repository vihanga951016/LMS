package com.vp.lms.services.common.security.securityImpls;

import com.vp.lms.services.common.security.AuthenticationManagerService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationManagerServiceImpl implements AuthenticationManagerService {

    private AuthenticationManager authenticationManager;

    @Override
    public void authentication(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }
}
