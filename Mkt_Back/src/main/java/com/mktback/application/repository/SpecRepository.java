package com.mktback.application.repository;

import com.mktback.application.domain.Spec;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Spec entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecRepository extends JpaRepository<Spec, String> {}
