package com.cegeka.academy.service.dto;

import java.util.Date;
import java.util.Objects;

public class UserChallengeDTO {

    private Long id;
    private UserDTO user;
    private InvitationChallengeDTO invitation;
    private ChallengeDTO challenge;
    private String status;
    private double points;
    private Date startTime;
    private Date endTime;
    private ChallengeAnswerDTO challengeAnswer;

    public UserChallengeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public InvitationChallengeDTO getInvitation() {
        return invitation;
    }

    public void setInvitation(InvitationChallengeDTO invitation) {
        this.invitation = invitation;
    }

    public ChallengeDTO getChallenge() {
        return challenge;
    }

    public void setChallenge(ChallengeDTO challenge) {
        this.challenge = challenge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public ChallengeAnswerDTO getChallengeAnswer() {
        return challengeAnswer;
    }

    public void setChallengeAnswer(ChallengeAnswerDTO challengeAnswer) {
        this.challengeAnswer = challengeAnswer;
    }

    @Override
    public String toString() {
        return "UserChallengeDTO{" +
                "id=" + id +
                ", user=" + user +
                ", invitation=" + invitation +
                ", challenge=" + challenge +
                ", status='" + status + '\'' +
                ", points=" + points +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", challengeAnswer=" + challengeAnswer +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserChallengeDTO)) return false;
        UserChallengeDTO that = (UserChallengeDTO) o;
        return Double.compare(that.getPoints(), getPoints()) == 0 &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getInvitation(), that.getInvitation()) &&
                Objects.equals(getChallenge(), that.getChallenge()) &&
                Objects.equals(getStatus(), that.getStatus()) &&
                Objects.equals(getStartTime(), that.getStartTime()) &&
                Objects.equals(getEndTime(), that.getEndTime()) &&
                Objects.equals(getChallengeAnswer(), that.getChallengeAnswer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getInvitation(), getChallenge(), getStatus(), getPoints(), getStartTime(), getEndTime(), getChallengeAnswer());
    }
}
