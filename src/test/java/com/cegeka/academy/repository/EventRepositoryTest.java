package com.cegeka.academy.repository;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.*;
import com.cegeka.academy.repository.util.TestsRepositoryUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AcademyProjectApp.class)
@Transactional
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private User user;
    private Event event;
    private Invitation invitation;
    private Address address;
    private Set<Category> categories;
    private Set<Category> categoriesHelper;
    private Category category1, category2;

    @BeforeEach
    public void init() {
        user = TestsRepositoryUtil.createUser("login", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        userRepository.save(user);
        address = TestsRepositoryUtil.createAddress("Romania", "Bucuresti", "Splai", "333", "Casa", "Casa magica");
        addressRepository.saveAndFlush(address);
        category1 = TestsRepositoryUtil.createCategory("Sport", "Liber pentru toate varstele!");
        Category category3 = TestsRepositoryUtil.createCategory("Arta", "Expozitii de arta");
        category2 = TestsRepositoryUtil.createCategory("Sociale", "Actiuni caritabile");
        categoryRepository.save(category1);
        categoryRepository.save(category3);
        categoryRepository.save(category2);
        categories = new HashSet<>();
        categories.add(category1);
        categories.add(category3);
        categoriesHelper = new HashSet<>();
        categoriesHelper.add(category1);
        categoriesHelper.add(category2);


    }

    @Test
    public void testAddEvent() {
        event = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", true, address, user, categories);
        eventRepository.save(event);
        Event eventTest = eventRepository.findTopByOrderByIdDesc();
        assertThat(eventTest.isPublicEvent()).isEqualTo(true);
        assertThat(eventTest.getName()).isEqualTo(event.getName());
    }

    @Test
    public void testFindAllByIsPublicIsTrue() {
        List<Event> existingEvents = eventRepository.findAllByPublicEventIsTrue();
        for (int i = 0; i < 5; i++) {
            Event event = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", true, address, user, categories);
            event.setPublicEvent(i % 2 == 0);
            eventRepository.save(event);
        }

        List<Event> publicEvents = eventRepository.findAllByPublicEventIsTrue();
        assertThat(publicEvents.size()).isEqualTo(3 + existingEvents.size());
    }

    @Test
    public void testFindByUsers_id() {

        Event event = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", true, address, user, categories);
        eventRepository.save(event);
        user.getEvents().add(event);
        userRepository.save(user);

        Event event1 = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", true, address, user, categories);
        eventRepository.save(event1);
        user.getEvents().add(event1);
        userRepository.save(user);

        List<Event> events = eventRepository.findByUsers_id(user.getId());
        assertThat(events.size()).isEqualTo(2);
    }


    @Test
    public void testFindByCategory_id() {
        Event event1 = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", true, address, user, categories);
        eventRepository.save(event1);
        Event event2 = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", false, address, user, categories);
        eventRepository.save(event2);
        List<Event> eventsCategory_1 = eventRepository.findAllByCategories_id(category1.getId());
        assertThat(eventsCategory_1.size()).isEqualTo(2);
    }

    @Test
    public void testFindAllByEvents_id() {
        User user1 = TestsRepositoryUtil.createUser("login2", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa2");

        Event event = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", true, address, user, categories);
        eventRepository.save(event);

        user.getEvents().add(event);
        userRepository.save(user);

        user1.getEvents().add(event);
        userRepository.save(user1);

        List<User> users = userRepository.findAllByEvents_id(event.getId());
        assertThat(users.size()).isEqualTo(2);

    }

    @Test
    public void testfindAllByIsPublicIsTrueAndCategoriesIn() {

        Event event = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", true, address, user, categories);
        eventRepository.save(event);

        Event event1 = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", true, address, user, categories);
        eventRepository.save(event1);

        List<Category> categ = new ArrayList<>();
        categ.add(category2);
        categ.add(category1);
        List<Event> events = eventRepository.findDistinctByPublicEventIsTrueAndCategoriesIn(categ);
        assertThat(events.size()).isEqualTo(2);

    }

    @Test
    public void testfindAllByIsPublicIsTrueAndCategoriesInIsWorkingWithPrivateEvent() {

        Event event = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", false, address, user, categories);
        eventRepository.save(event);
        List<Category> categ = new ArrayList<>();
        categ.add(category2);
        categ.add(category1);
        List<Event> events = eventRepository.findDistinctByPublicEventIsTrueAndCategoriesIn(categ);
        assertThat(events.size()).isEqualTo(0);

    }


}
