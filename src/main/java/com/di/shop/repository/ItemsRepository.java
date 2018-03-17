package com.di.shop.repository;

import com.di.shop.domain.Items;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Items entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {

}
