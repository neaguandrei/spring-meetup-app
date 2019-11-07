package com.cegeka.academy.service.dto;

import com.cegeka.academy.domain.AgeIntervals;
import com.cegeka.academy.domain.Event;

public class UserStatisticsDTO {


    private long id;
    private AgeIntervals ageInterval;
    private Event event;
    private int noPeople;
    private String gender;

    public UserStatisticsDTO(long id, AgeIntervals ageInterval, Event event, int noPeople, String gender) {
        this.id = id;
        this.ageInterval = ageInterval;
        this.event = event;
        this.noPeople = noPeople;
        this.gender = gender;
    }

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
