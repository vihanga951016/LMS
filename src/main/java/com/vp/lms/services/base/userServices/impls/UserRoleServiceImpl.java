package com.vp.lms.services.base.userServices.impls;

import com.vp.lms.common.http.bean.HttpResponse;
import com.vp.lms.beans.user.UserRoleBean;
import com.vp.lms.common.http.locale.LocaleService;
import com.vp.lms.repository.repositories.userRepos.UserRoleRepository;
import com.vp.lms.services.base.userServices.UserRoleServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@Service
@SuppressWarnings("Duplicates")
public class UserRoleServiceImpl implements UserRoleServices {

    private final LocaleService localeService;
    private final UserRoleRepository userRoleRepository;

    @Override
    public ResponseEntity addRole(UserRoleBean role, HttpServletRequest request) {

        UserRoleBean existingRole = checkUserRoleAlreadyExist(role.getId());

        if(existingRole != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                    .responseFail(localeService.getMessage("role.already.exist", request)));
        }

        UserRoleBean newRole =  userRoleRepository
                .save(role);

        return ResponseEntity.ok().body(new HttpResponse<>().responseOk(newRole));
    }

    @Override
    public ResponseEntity disableRole(String id, HttpServletRequest request) {

        UserRoleBean existingRole = checkUserRoleAlreadyExist(id);

        if(existingRole == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                    .responseFail(localeService.getMessage("role.not.found", request)));
        }

        existingRole.setDisabled(!existingRole.isDisabled());
        userRoleRepository.save(existingRole);

        return ResponseEntity.ok().body(new HttpResponse<>().responseOk(true));
    }

    @Override
    public ResponseEntity deleteRole(String id, HttpServletRequest request) {
        UserRoleBean existingRole = checkUserRoleAlreadyExist(id);

        if(existingRole == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new HttpResponse<>()
                    .responseFail(localeService.getMessage("role.not.found", request)));
        }

        existingRole.setDeleted(!existingRole.isDeleted());
        userRoleRepository.save(existingRole);

        return ResponseEntity.ok().body(new HttpResponse<>().responseOk(true));
    }

    private UserRoleBean checkUserRoleAlreadyExist(String role){
        return userRoleRepository.getRoleByName(role);
    }
}
