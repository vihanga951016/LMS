package com.vp.lms.services.base.userServices.impls;

import com.vp.lms.beans.user.UserBean;
import com.vp.lms.common.FileConstant;
import com.vp.lms.common.http.bean.HttpResponse;
import com.vp.lms.common.http.locale.LocaleService;
import com.vp.lms.repository.repositories.userRepos.UserRepository;
import com.vp.lms.repository.repositories.userRepos.UserRoleRepository;
import com.vp.lms.services.base.userServices.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@AllArgsConstructor
@Service
@SuppressWarnings("Duplicates")
public class UserServiceImpl implements UserService {

    private BCryptPasswordEncoder passwordEncoder;
    private org.slf4j.Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final LocaleService localeService;

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    //TODO - in password parameter encode the password
    //TODO - save profile image
    @Override
    public ResponseEntity register(String name, Date dob, String nic, String address, String phone,
                              String email, String password, MultipartFile profileImageUrl, Date lastLogin,
                              Date lastLoginDateDisplay, Date joinDate, String role,
                              HttpServletRequest request) throws IOException {

        UserBean existingUserWithEmail = userRepository.getUserByEmail(email);

        if(existingUserWithEmail != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                    .responseFail(localeService.getMessage("user.email.exist", request)));
        }

        UserBean existingUserWithNic = userRepository.getUserByNIC(nic);

        if(existingUserWithNic != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                    .responseFail(localeService.getMessage("user.nic.exist", request)));
        }

        UserBean newUser = new UserBean();

        newUser.setName(name);
        newUser.setDob(dob);
        newUser.setNic(nic);
        newUser.setAddress(address);
        newUser.setPhone(phone);
        newUser.setEmail(email);
        newUser.setPassword(encodePassword(password));
        newUser.setProfileImageUrl(saveProfileImage(nic, profileImageUrl));
        newUser.setLastLoginDate(lastLogin);
        newUser.setLastLoginDateDisplay(lastLoginDateDisplay);
        newUser.setJoinDate(joinDate);
        newUser.setAccountIsActive(false);
        newUser.setAccountIsExpired(false);
        newUser.setUserRoleBean(userRoleRepository.getRoleByName(role));

        userRepository.save(newUser);

        return ResponseEntity.ok().body(new HttpResponse<>().responseOk(newUser));
    }

    @Override
    public ResponseEntity update(UserBean userBean, HttpServletRequest request) {
        return null;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String saveProfileImage(String nic, MultipartFile profileImage) throws IOException {
        if(profileImage != null) {
            Path folder = Paths.get(FileConstant.USER_FOLDER + nic).toAbsolutePath().normalize();
            if(!Files.exists(folder)) {
                Files.createDirectories(folder);
                LOGGER.info(FileConstant.DIRECTORY_CREATED + folder);
            }

            Files.deleteIfExists(Paths.get(folder + nic + "." + FileConstant.JPG_EXTENSION ));

            Files.copy(profileImage.getInputStream(),folder.resolve(nic + "." + FileConstant.JPG_EXTENSION)
                    , REPLACE_EXISTING);

            return setProfileImageUrl(nic);
        } else {
            return null;
        }
    }

    private String setProfileImageUrl(String nic){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(FileConstant.DEFAULT_USER_IMAGE_PATH + nic
                        + "/" + nic + "." + FileConstant.JPG_EXTENSION).toUriString();
    }

}
