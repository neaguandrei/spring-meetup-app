package com.cegeka.academy.domain;

import javax.persistence.*;

@Entity
@Table(name = "user_statistics")
public class UserStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "age_intervals_id", referencedColumnName = "id")
    private AgeIntervals ageInterval;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @Column(name = "no_people")
    private int noPeople;


    @Column(name = "gender")
    private String gender;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AgeIntervals getAgeInterval() {
        return ageInterval;
    }

    public void setAgeInterval(AgeIntervals ageInterval) {
        this.ageInterval = ageInterval;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getNoPeople() {
        return noPeople;
    }

    public void setNoPeople(int noPeople) {
        this.noPeople = noPeople;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
