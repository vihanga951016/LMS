package com.vp.lms.services.base.userServices;

import com.vp.lms.beans.user.UserRoleBean;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface UserRoleServices {

    ResponseEntity addRole(UserRoleBean role, HttpServletRequest request);
    ResponseEntity disableRole(String id, HttpServletRequest request);
    ResponseEntity deleteRole(String id, HttpServletRequest request);
}
