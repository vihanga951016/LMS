package com.vp.lms.services.base.userServices.impls;

import com.vp.lms.beans.property.InstituteBean;
import com.vp.lms.beans.user.UserBean;
import com.vp.lms.beans.user.requests.UserLoginBean;
import com.vp.lms.common.ApplicationConstant;
import com.vp.lms.common.FileConstant;
import com.vp.lms.common.enums.JwtTypes;
import com.vp.lms.common.http.bean.HttpResponse;
import com.vp.lms.common.http.locale.LocaleService;
import com.vp.lms.common.security.HashUtils;
import com.vp.lms.repository.repositories.propertiesRepos.InstituteRepository;
import com.vp.lms.repository.repositories.userRepos.UserRepository;
import com.vp.lms.repository.repositories.userRepos.UserRoleRepository;
import com.vp.lms.services.base.userServices.UserService;
import com.vp.lms.services.common.security.utils.JwtTokenUtil;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@SuppressWarnings("Duplicates")
public class UserServiceImpl implements UserService {

    private static Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private final LocaleService localeService;
    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final InstituteRepository instituteRepository;

    @Autowired
    public UserServiceImpl(LocaleService localeService,
                           JwtTokenUtil jwtTokenUtil, UserRepository userRepository,
                           UserRoleRepository userRoleRepository,
                           InstituteRepository instituteRepository) {
        this.localeService = localeService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.instituteRepository = instituteRepository;
    }

    @Override
    public ResponseEntity userLogin(UserLoginBean userLoginBean, HttpServletRequest request) {
        UserBean user = userRepository.getUserEntityByEmail(userLoginBean.getEmail());

        if(user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                    .responseFail(localeService.getMessage("user.not.found.email", request)));
        }

        if(HashUtils.checkEncrypted(userLoginBean.getPassword(), user.getPassword())){
            LOGGER.info("Password hash matched | " + user.getId() + " | name |"
                    + user.getName() +" | username |" + userLoginBean.getEmail());
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

        user.setToken(createUserLoginToken(user, userLoginBean));

        return ResponseEntity.ok().body(new HttpResponse<>().responseOk(user));
    }
    
    @Override
    public ResponseEntity register(String name, String nic, String address, String phone,
                              String email, String password, MultipartFile profileImageUrl, String instituteName,
                              String instituteAddress, Integer subscription, String instituteMail,
                                   String instituteContact, HttpServletRequest request) throws IOException {

        UserBean existingUserWithEmail = userRepository.getUserByEmail(email);

        if(existingUserWithEmail != null) {
            LOGGER.info("user.email.exist");
            LOGGER.info("Request: "+ request);
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
        newInstitute.setContact("1234567890");
        newInstitute.setDeactivated(false);
        newInstitute.setNotAvailable(false);

        InstituteBean newlyRegisteredInstitute = instituteRepository.save(newInstitute);

        UserBean newUser = new UserBean();

        newUser.setName(name);
        newUser.setDob(new Date());
        newUser.setNic(nic);
        newUser.setAddress("");
        newUser.setPhone("");
        newUser.setEmail(email);
        newUser.setPassword(HashUtils.hash(password));
        newUser.setProfileImageUrl(saveProfileImage(nic, profileImageUrl));
        newUser.setLastLoginDate(new Date());
        newUser.setLastLoginDateDisplay(new Date());
        newUser.setJoinDate(new Date());
        newUser.setAccountIsDeactivated(false);
        newUser.setAccountIsExpired(false);
        newUser.setUserRoleBean(userRoleRepository.getRoleByName("head_master"));
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

    private String createUserLoginToken(UserBean userBean, UserLoginBean userLoginBean){
        Map<String, Object> claims = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        long expireTime = calendar.getTimeInMillis();
        LOGGER.info("User login expires on >>> " + calendar.getTime().toString());

        String userType = null;

        LOGGER.info(userBean.getJwtType() + " is trying to login");

        if (userBean.getJwtType().equals(JwtTypes.SUPER_ADMIN.name())){
            userType = JwtTypes.SUPER_ADMIN.name();
        } else if(userBean.getJwtType().equals(JwtTypes.ADMIN.name())) {
            userType = JwtTypes.ADMIN.name();
        } else if(userBean.getJwtType().equals(JwtTypes.USER.name())) {
            userType = JwtTypes.USER.name();
        } else {
            LOGGER.error("User type is not defined");
        }

        claims.put(ApplicationConstant.JWT_USER_ID, userBean.getId());
        claims.put(ApplicationConstant.JWT_INSTITUTE_ID, userBean.getInstituteBean().getId());

        return jwtTokenUtil.generateTokenWithExp
                (new User(userType, userLoginBean.getPassword(), new ArrayList<>()), expireTime, claims);
    }

    private String saveProfileImage(String nic, MultipartFile profileImage) {
        try {
            if(profileImage != null) {
                Path folder = Paths.get(FileConstant.USER_FOLDER + nic).toAbsolutePath().normalize();
                if(!Files.exists(folder)) {
                    Files.createDirectories(folder);
                    LOGGER.info(FileConstant.DIRECTORY_CREATED + folder);
                }

                LOGGER.info("Existing path: "+ Paths.get(folder + nic + "." + FileConstant.JPG_EXTENSION ));
                Files.deleteIfExists(Paths.get(folder + nic + "." + FileConstant.JPG_EXTENSION ));
                LOGGER.info("Deleted");

                Files.copy(profileImage.getInputStream(),folder.resolve(nic + "." + FileConstant.JPG_EXTENSION)
                        , REPLACE_EXISTING);

                return setProfileImageUrl(nic);
            } else {
                return null;
            }
        } catch (Exception e){
            LOGGER.error(e);
        }

        return "error";
    }

    private String setProfileImageUrl(String nic){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(FileConstant.DEFAULT_USER_IMAGE_PATH + nic
                        + "/" + nic + "." + FileConstant.JPG_EXTENSION).toUriString();
    }

}
