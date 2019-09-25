package com.innvo.repository;
import com.innvo.domain.Parent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Parent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {

}
