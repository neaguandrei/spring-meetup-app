package com.cegeka.academy.repository.util;

import com.cegeka.academy.domain.*;

import java.util.Date;
import java.util.Set;

public class TestsRepositoryUtil {

    public static Address createAddress(String country, String city, String street, String number, String details, String name) {
        Address address = new Address();
        address.setCity(city);
        address.setCountry(country);
        address.setStreet(street);
        address.setDetails(details);
        address.setNumber(number);
        address.setName(name);
        return address;
    }

    public static Event createEvent(String description, String name, boolean isPublic, Address address, User user, Set<Category> categories) {
        Event event = new Event();
        event.setDescription(description);
        event.setName(name);
        event.setPublicEvent(isPublic);
        event.setOwner(user);
        event.setCategories(categories);
        return event;
    }

    public static User createUser(String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        return user;
    }

    public static Category createCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return category;
    }

    public static Invitation createInvitation(String status, String description, Event event, User user) {
        Invitation invitation = new Invitation();
        invitation.setStatus(status);
        invitation.setDescription(description);
        invitation.setUser(user);
        invitation.setEvent(event);
        return invitation;
    }

    public static Group createGroup(String name, String description) {
        Group group = new Group();
        group.setName(name);
        group.setDescription(description);
        return group;
    }

    public static GroupUserRole createGroupUserRole(User user, Group group, Role role) {
        GroupUserRole groupUserRole = new GroupUserRole();
        groupUserRole.setUser(user);
        groupUserRole.setGroup(group);
        groupUserRole.setRole(role);
        return groupUserRole;
    }

    public static Role createRole(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }

    public static Challenge createChallenge(ChallengeCategory challengeCategory, String description, User creator,
                                            String status, Date startDate, Date endDate, Double points) {
        Challenge challenge = new Challenge();
        challenge.setChallengeCategory(challengeCategory);
        challenge.setDescription(description);
        challenge.setCreator(creator);
        challenge.setStatus(status);
        challenge.setEndDate(startDate);
        challenge.setStartDate(endDate);
        challenge.setPoints(points);

        return challenge;
    }

    public static ChallengeCategory createChallengeCategory (String description, String name) {
        ChallengeCategory challengeCategory = new ChallengeCategory();
        challengeCategory.setDescription(description);
        challengeCategory.setName(name);

        return challengeCategory;
    }

    public static UserChallenge createUserChallenge (Challenge challenge, User user, Double points, String status,
                                                     Invitation invitation, Date startTIme, Date endTime) {
        UserChallenge userChallenge = new UserChallenge();
        userChallenge.setChallengeAnswer(null);
        userChallenge.setChallenge(challenge);
        userChallenge.setUser(user);
        userChallenge.setPoints(points);
        userChallenge.setStatus(status);
        userChallenge.setInvitation(invitation);
        userChallenge.setStartTime(startTIme);
        userChallenge.setEndTime(endTime);

        return userChallenge;
    }
}
