package com.cegeka.academy.repository;

import com.cegeka.academy.domain.GroupUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;



@Repository
public interface GroupUserRoleRepository extends JpaRepository<GroupUserRole, Long> {
    List<GroupUserRole> findAllByGroupId(Long groupId);


    @Query("SELECT u FROM GroupUserRole u WHERE u.user.id=?1")
    List<GroupUserRole> findAllByUserId(Long id);

    GroupUserRole findOneByGroupIdAndUserId (Long groupId, Long userId);


    @Query("SELECT u from GroupUserRole u where u.user.id<>?1")
    List<GroupUserRole> findGroupsUserNotMember(Long id);

}
