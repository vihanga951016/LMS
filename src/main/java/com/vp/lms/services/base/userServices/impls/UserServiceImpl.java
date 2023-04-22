package com.vp.lms.services.base.userServices.impls;

import com.vp.lms.beans.property.InstituteBean;
import com.vp.lms.beans.user.UserBean;
import com.vp.lms.common.FileConstant;
import com.vp.lms.common.enums.JwtTypes;
import com.vp.lms.common.http.bean.HttpResponse;
import com.vp.lms.common.http.locale.LocaleService;
import com.vp.lms.common.security.HashUtils;
import com.vp.lms.repository.repositories.propertiesRepos.InstituteRepository;
import com.vp.lms.repository.repositories.userRepos.UserRepository;
import com.vp.lms.repository.repositories.userRepos.UserRoleRepository;
import com.vp.lms.services.base.userServices.UserService;
import com.vp.lms.services.common.security.AuthenticationManagerService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@SuppressWarnings("Duplicates")
public class UserServiceImpl implements UserService {

    private static Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private final LocaleService localeService;
    private final AuthenticationManagerService authenticationManagerService;

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final InstituteRepository instituteRepository;

    @Autowired
    public UserServiceImpl(LocaleService localeService,
                           AuthenticationManagerService authenticationManagerService,
                           UserRepository userRepository,
                           UserRoleRepository userRoleRepository,
                           InstituteRepository instituteRepository) {
        this.localeService = localeService;
        this.authenticationManagerService = authenticationManagerService;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.instituteRepository = instituteRepository;
    }

    @Override
    public ResponseEntity userLogin(String email, String password, HttpServletRequest request) {
        authenticationManagerService.authentication(email, password);
        UserBean user = userRepository.getUserEntityByEmail(email);
//        UserPrinciplesBean<UserBean> userPrinciples = new UserPrinciplesBean<>(user);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                    .responseFail(localeService.getMessage("user.not.found.email", request)));
        }

        if(HashUtils.checkEncrypted(password, user.getPassword())){
            LOGGER.info("Password hash matched | " + user.getId() + " | name |"
                    + user.getName() +" | username |" + email);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                    .responseFail(localeService.getMessage("user.password.not.match", request)));
        }

        if(user.isAccountIsDeactivated()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                    .responseFail(localeService.getMessage("user.account.deactivated", request)));
        }

        if(user.isAccountIsExpired()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                    .responseFail(localeService.getMessage("user.account.expired", request)));
        }

        //TODO - set token to user bean.

        return ResponseEntity.ok().body(new HttpResponse<>().responseOk(user));
    }

    @Override
    public ResponseEntity register(String name, Date dob, String nic, String address, String phone,
                              String email, String password, MultipartFile profileImageUrl, Date lastLogin,
                              Date lastLoginDateDisplay, Date joinDate, String role, String instituteName,
                              String instituteAddress, Integer subscription, String instituteMail,
                                   String instituteContact, HttpServletRequest request) throws IOException {

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

        InstituteBean existingInstitute = instituteRepository.getInstituteByEmail(instituteMail);

        if(existingInstitute != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                    .responseFail(localeService.getMessage("institute.already.registered", request)));
        }

        InstituteBean newInstitute = new InstituteBean();

        newInstitute.setName(instituteName);
        newInstitute.setAddress(instituteName);
        newInstitute.setSubscription(subscription);
        newInstitute.setEmail(instituteMail);
        newInstitute.setContact(instituteContact);
        newInstitute.setDeactivated(false);
        newInstitute.setNotAvailable(false);

        InstituteBean newlyRegisteredInstitute = instituteRepository.save(newInstitute);

        UserBean newUser = new UserBean();

        newUser.setName(name);
        newUser.setDob(dob);
        newUser.setNic(nic);
        newUser.setAddress(address);
        newUser.setPhone(phone);
        newUser.setEmail(email);
        newUser.setPassword(HashUtils.hash(password));
        newUser.setProfileImageUrl(saveProfileImage(nic, profileImageUrl));
        newUser.setLastLoginDate(lastLogin);
        newUser.setLastLoginDateDisplay(lastLoginDateDisplay);
        newUser.setJoinDate(joinDate);
        newUser.setAccountIsDeactivated(false);
        newUser.setAccountIsExpired(false);
        newUser.setUserRoleBean(userRoleRepository.getRoleByName(role));
        newUser.setInstituteBean(
                new InstituteBean(newlyRegisteredInstitute.getId(),
                        newlyRegisteredInstitute.getName()));
        newUser.setJwtType(JwtTypes.SUPER_ADMIN.name());

        userRepository.save(newUser);

        return ResponseEntity.ok().body(new HttpResponse<>().responseOk(newUser));
    }

    @Override
    public ResponseEntity update(UserBean userBean, HttpServletRequest request) {
        return null;
    }

    private String createUserLoginToken(UserBean userBean){
        Map<String, Object> claims = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        long expireTime = calendar.getTimeInMillis();
        LOGGER.info("User login expires on >>> " + calendar.getTime().toString());

        String userType = null;

        if (userBean.getJwtType().equals(JwtTypes.SUPER_ADMIN.name())){
            userType = JwtTypes.SUPER_ADMIN.name();
        }

        return null;
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
