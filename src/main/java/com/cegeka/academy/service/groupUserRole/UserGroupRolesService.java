package com.cegeka.academy.service.groupUserRole;

import com.cegeka.academy.domain.GroupUserRole;

import java.util.List;

public interface UserGroupRolesService {

    List<GroupUserRole> getById(Long userId);
    void addMember (GroupUserRole user);
    void removeMember (GroupUserRole user);

    List<GroupUserRole> findGroupsUserNotMember(Long userId);
}
