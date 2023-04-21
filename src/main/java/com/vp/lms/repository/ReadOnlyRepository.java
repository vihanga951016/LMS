package com.vp.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadOnlyRepository<T, ID> extends JpaRepository<T, ID> {
}
