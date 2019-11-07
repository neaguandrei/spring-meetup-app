package com.cegeka.academy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user_challenge")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserChallenge implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne
    @JoinColumn(name = "invitation_id", referencedColumnName = "id")
    private Invitation invitation;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "challenge_id", referencedColumnName = "id")
    private Challenge challenge;

    @NotNull
    @Size(max = 45)
    @Column(name = "status")
    private String status;

    @NotNull
    @Column(name = "points")
    private double points;

    @NotNull
    @Column(name = "start_time")
    private Date startTime;

    @NotNull
    @Column(name = "end_time")
    private Date endTime;

    @ManyToOne
    @JoinColumn(name = "challenge_answer_id", referencedColumnName = "id")
    private ChallengeAnswer challengeAnswer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Invitation getInvitation() { return invitation; }

    public void setInvitation(Invitation invitation) { this.invitation = invitation; }

    public Challenge getChallenge() { return challenge; }

    public void setChallenge(Challenge challenge) { this.challenge = challenge; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public double getPoints() { return points; }

    public void setPoints(double points) { this.points = points; }

    public Date getStartTime() { return startTime; }

    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public Date getEndTime() { return endTime; }

    public void setEndTime(Date endTime) { this.endTime = endTime; }

    public ChallengeAnswer getChallengeAnswer() {
        return challengeAnswer;
    }

    public void setChallengeAnswer(ChallengeAnswer challengeAnswer) {
        this.challengeAnswer = challengeAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserChallenge)) return false;
        UserChallenge that = (UserChallenge) o;
        return Double.compare(that.getPoints(), getPoints()) == 0 &&
                getId().equals(that.getId()) &&
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

    @Override
    public String toString() {
        return "UserChallenge{" +
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
}
