package com.cegeka.academy.service.dto;

import com.cegeka.academy.domain.Group;
import com.cegeka.academy.domain.Role;
import com.cegeka.academy.domain.User;

public class GroupUserRoleDTO {

    private long id;
    private long userId;
    private long groupId;
    private long roleId;

    public GroupUserRoleDTO(){

    }

    public GroupUserRoleDTO(long id, long userId, long groupId, long roleId) {
        this.id = id;
        this.userId = userId;
        this.groupId = groupId;
        this.roleId = roleId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }
}
