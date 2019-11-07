package com.cegeka.academy.repository;

import com.cegeka.academy.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the {@link Group} entity.
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Group findByName(String groupName);
//    Group findGroupById(Long id);
}
