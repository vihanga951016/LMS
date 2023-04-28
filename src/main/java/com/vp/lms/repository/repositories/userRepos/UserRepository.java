package com.vp.lms.repository.repositories.userRepos;

import com.vp.lms.beans.user.UserBean;
import com.vp.lms.repository.ReadOnlyRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends ReadOnlyRepository<UserBean, Integer> {

    @Query("SELECT new UserBean(u.id) FROM UserBean u WHERE u.id=:id AND u.accountIsExpired = false")
    UserBean getUserById(@Param("id") Integer id);

    @Query("SELECT u FROM UserBean u WHERE u.id=:id AND u.accountIsExpired = false")
    UserBean getUserEntityById(@Param("id") Integer id);

    @Query("SELECT new UserBean(u.id) FROM UserBean u WHERE u.email=:email AND u.accountIsExpired = false")
    UserBean getUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM UserBean u WHERE u.email=:email AND u.accountIsExpired = false")
    UserBean getUserEntityByEmail(@Param("email") String email);

    @Query("SELECT new UserBean(u.id) FROM UserBean u WHERE u.nic=:nic AND u.accountIsExpired = false")
    UserBean getUserByNIC(@Param("nic") String nic);

    @Query("SELECT u FROM UserBean u WHERE u.instituteBean.id=:iId")
    List<UserBean> getAllUsersByInstitute(@Param("iId") Integer id);
}
