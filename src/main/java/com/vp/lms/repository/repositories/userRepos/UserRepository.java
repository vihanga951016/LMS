package com.vp.lms.repository.repositories.userRepos;

import com.vp.lms.beans.user.UserBean;
import com.vp.lms.repository.ReadOnlyRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends ReadOnlyRepository<UserBean, Integer> {

    @Query("SELECT new UserBean(u.id) FROM UserBean u WHERE u.id=:id AND u.accountIsExpired = false")
    UserBean getUserById(@Param("id") Integer id);

    @Query("SELECT new UserBean(u.id) FROM UserBean u WHERE u.email=:email AND u.accountIsExpired = false")
    UserBean getUserByEmail(@Param("email") String email);

    @Query("SELECT new UserBean(u.id) FROM UserBean u WHERE u.nic=:nic AND u.accountIsExpired = false")
    UserBean getUserByNIC(@Param("nic") String nic);
}
