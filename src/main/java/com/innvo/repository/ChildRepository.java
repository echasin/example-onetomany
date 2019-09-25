package com.innvo.repository;
import com.innvo.domain.Child;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Child entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

}
