package com.cegeka.academy.service.dto;

import java.util.Objects;

public class InvitationChallengeDTO {

    private Long id;
    private String status;
    private UserDTO user;

    public InvitationChallengeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "InvitationChallengeDTO{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvitationChallengeDTO)) return false;
        InvitationChallengeDTO that = (InvitationChallengeDTO) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getStatus(), that.getStatus()) &&
                Objects.equals(getUser(), that.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStatus(), getUser());
    }
}
