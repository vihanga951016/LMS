package com.vp.lms.repository.repositories.userRepos;

import com.vp.lms.beans.user.UserRoleAuthoritiesBean;
import com.vp.lms.repository.ReadOnlyRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRoleAuthoritiesRepository extends ReadOnlyRepository<UserRoleAuthoritiesBean, Integer> {

    @Query("SELECT ura FROM UserRoleAuthoritiesBean ura WHERE ura.role.id=:role AND ura.authorities.authority=:permission")
    UserRoleAuthoritiesBean getEntityByRole(@Param("role") String role, @Param("permission") String permission);
}
