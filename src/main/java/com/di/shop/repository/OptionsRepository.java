package com.di.shop.repository;

import com.di.shop.domain.Options;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Options entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OptionsRepository extends JpaRepository<Options, Long> {

}
