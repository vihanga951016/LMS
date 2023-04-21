package com.vp.lms.services.base.userServices;

import com.vp.lms.beans.user.UserBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

public interface UserService {

    ResponseEntity register(String name, Date dob, String nic, String address, String phone,
                            String email, String password, MultipartFile profileImageUrl, Date lastLogin,
                            Date lastLoginDateDisplay, Date joinDate, String role,
                            HttpServletRequest request) throws IOException;
    ResponseEntity update(UserBean userBean, HttpServletRequest request);
}
