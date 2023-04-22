package com.vp.lms.repository.repositories.propertiesRepos;

import com.vp.lms.beans.property.InstituteBean;
import com.vp.lms.repository.ReadOnlyRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface InstituteRepository extends ReadOnlyRepository<InstituteBean, Integer> {

    @Query("SELECT i FROM InstituteBean i WHERE i.email=:email AND i.notAvailable = false")
    InstituteBean getInstituteByEmail(@Param("email") String email);
}
