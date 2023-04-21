package com.vp.lms.repository.repositories.userRepos;

import com.vp.lms.beans.user.UserRoleBean;
import com.vp.lms.repository.ReadOnlyRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends ReadOnlyRepository<UserRoleBean, String> {

    @Query("SELECT ur FROM UserRoleBean ur WHERE ur.id=:name AND ur.deleted = false")
    UserRoleBean getRoleByName(@Param("name") String name);
}
