package com.vp.lms.services.base.userServices;

import com.vp.lms.beans.user.UserBean;
import com.vp.lms.beans.user.requests.UserLoginBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface UserService {

    ResponseEntity userLogin(UserLoginBean userLoginBean, HttpServletRequest request);

    ResponseEntity register(String name, String nic, String address, String phone,
                            String email, String password, MultipartFile profileImageUrl, String instituteName,
                            String instituteAddress, Integer subscription, String instituteMail, String contact,
                            HttpServletRequest request) throws IOException;

//    String name, Date dob, String nic, String address, String phone,
//    String email, String password, MultipartFile profileImageUrl, Date lastLogin,
//    Date lastLoginDateDisplay, Date joinDate, String role, String instituteName,
//    String instituteAddress, Integer subscription, String instituteMail, String contact,
//    HttpServletRequest request

//    ResponseEntity register(String name, String nic,
//                            String email, String password, MultipartFile profileImageUrl, String instituteName,
//                            Integer subscription, String instituteMail,
//                            HttpServletRequest request) throws IOException;

    ResponseEntity update(UserBean userBean, HttpServletRequest request);
}
