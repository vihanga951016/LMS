package com.vp.lms.repository.repositories.userRepos;

import com.vp.lms.beans.user.UserAuthoritiesBean;
import com.vp.lms.repository.ReadOnlyRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserAuthoritiesRepository extends ReadOnlyRepository<UserAuthoritiesBean, Integer> {

    @Query("SELECT ua FROM UserAuthoritiesBean ua WHERE ua.id=:id AND ua.deleted = false")
    UserAuthoritiesBean getAuthorityById(@Param("id") Integer id);

    @Query("SELECT ua FROM UserAuthoritiesBean ua WHERE ua.authority=:name AND ua.deleted = false")
    UserAuthoritiesBean getAuthorityByName(@Param("name") Integer name);

    @Query("SELECT ua FROM UserAuthoritiesBean ua WHERE ua.deleted = false")
    List<UserAuthoritiesBean> getAllAuthorities();
}
