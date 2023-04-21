package com.vp.lms.controllers.base.userControllers;

import com.vp.lms.beans.user.UserRoleBean;
import com.vp.lms.services.base.userServices.UserRoleServices;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/role")
@AllArgsConstructor
public class UserRoleController {

    private final UserRoleServices userRoleServices;

    @PostMapping("/add")
    public ResponseEntity addUserRoles(@RequestBody UserRoleBean role, HttpServletRequest request) {
        return userRoleServices.addRole(role, request);
    }

    @PostMapping("/disable/id/{role}")
    public ResponseEntity disableUserRoles(@PathVariable("role") String role, HttpServletRequest request) {
        return userRoleServices.disableRole(role, request);
    }

    @PostMapping("/delete/id/{role}")
    public ResponseEntity deleteUserRoles(@PathVariable("role") String role, HttpServletRequest request) {
        return userRoleServices.deleteRole(role, request);
    }
}
