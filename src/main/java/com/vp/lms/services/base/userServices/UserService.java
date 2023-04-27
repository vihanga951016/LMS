package com.vp.lms.services.base.userServices;

import com.vp.lms.beans.user.UserBean;
import com.vp.lms.beans.user.requests.UserLoginBean;
import com.vp.lms.exceptions.AuthorizationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface UserService {

    ResponseEntity userLogin(UserLoginBean userLoginBean, HttpServletRequest request);

    ResponseEntity register(String name, String nic, String address, String phone,
                            String email, String password, MultipartFile profileImageUrl, String instituteName,
                            String instituteShortName, String instituteAddress, Integer subscription, String instituteMail,
                            String contact, HttpServletRequest request) throws IOException;


    ResponseEntity addUser(String name, String nic, String address, String phone,
                           String email, String password, MultipartFile profileImageUrl,
                           Integer instituteId, HttpServletRequest request) throws AuthorizationException;

    ResponseEntity update(Integer id, String name, String address, String phone,
                          MultipartFile profileImageUrl,HttpServletRequest request);
}
