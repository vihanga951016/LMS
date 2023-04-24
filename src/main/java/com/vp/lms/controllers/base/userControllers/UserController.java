package com.vp.lms.controllers.base.userControllers;

import com.vp.lms.beans.user.UserBean;
import com.vp.lms.beans.user.requests.UserLoginBean;
import com.vp.lms.services.base.userServices.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginBean userLoginBean, HttpServletRequest request) {
        return userService.userLogin(userLoginBean, request);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestParam("name")String name,
                                   String nic,
                                   String address,
                                   String phone,
                                   @RequestParam("email")String email,
                                   @RequestParam("password")String password,
                                   @RequestParam(value = "profileImageUrl", required = false) MultipartFile profileImageUrl,
                                   @RequestParam("instituteName")String instituteName,
                                   String instituteAddress,
                                   @RequestParam("subscription")Integer subscription,
                                   @RequestParam("instituteMail")String instituteMail,
                                   String instituteContact,
                                   HttpServletRequest request) throws IOException {
        return userService.register(name, nic, address, phone, email, password, profileImageUrl,
                 instituteName, instituteAddress, subscription, instituteMail, instituteContact, request);
    }
}
