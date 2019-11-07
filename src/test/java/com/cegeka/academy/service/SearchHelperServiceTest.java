package com.cegeka.academy.service;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.Address;
import com.cegeka.academy.domain.Category;
import com.cegeka.academy.domain.Event;
import com.cegeka.academy.domain.User;
import com.cegeka.academy.repository.AddressRepository;
import com.cegeka.academy.repository.CategoryRepository;
import com.cegeka.academy.repository.EventRepository;
import com.cegeka.academy.repository.UserRepository;
import com.cegeka.academy.repository.util.TestsRepositoryUtil;
import com.cegeka.academy.service.serviceValidation.SearchHelperService;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AcademyProjectApp.class)
@Transactional
public class SearchHelperServiceTest {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SearchHelperService searchHelperService;

    private Event event;
    private User user;
    private Address address;
    private Category category;

    @BeforeEach
    public void init() {

        categoryRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();
        User userOwner = TestsRepositoryUtil.createUser("login", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        userRepository.save(userOwner);
        address = TestsRepositoryUtil.createAddress("Romania", "Bucuresti", "Splai", "333", "Casa", "Casa magica");
        addressRepository.saveAndFlush(address);
        Set<Category> set = new HashSet<>();
        event = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", true, address, userRepository.findAll().get(0), set);
        category = TestsRepositoryUtil.createCategory("Ana", "description1");
        Category category1 = TestsRepositoryUtil.createCategory("MARIA", "description1");
        event.getCategories().add(category);
        category.getEvents().add(event);
        user = TestsRepositoryUtil.createUser("login1", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        user.getEvents().add(event);
        event.getUsers().add(user);
        userRepository.save(user);
        eventRepository.save(event);
        categoryRepository.save(category1);

    }

    @Test
    public void assertSearchEventsByCategoryNameIsWorkingWithValidArgument() throws NotFoundException {

        Set<Event> events = searchHelperService.searchEventsByCategoryName("Ana");
        assertThat(events.size()).isEqualTo(1);
    }

    @Test
    public void assertSearchEventsByCategoryNameIsWorkingWithInvalidArgument() {

        Assertions.assertThrows(NotFoundException.class, () -> searchHelperService.searchEventsByCategoryName("Ana1"));
    }

    @Test
    public void assertSearchEventsByCategoryNameIsWorkingWithNullArgument() {

        Assertions.assertThrows(NotFoundException.class, () -> searchHelperService.searchEventsByCategoryName(null));
    }

    @Test
    public void assertSearchEventsByCategoryNameIsWorkingWithValidArgumentButNoEvents() {

        Assertions.assertThrows(NotFoundException.class, () -> searchHelperService.searchEventsByCategoryName("MARIA"));
    }

    @Test
    public void assertSearchUserByInterestedEventsIsWorkingWithValidSet() throws NotFoundException {

        Set<Event> events = new HashSet<>();
        events.add(event);
        List<User> list = searchHelperService.searchUserByInterestedEvents(events);
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void assertSearchUserByInterestedEventsIsWorkingWithVEmptySet() throws NotFoundException {

        Set<Event> events = new HashSet<>();
        List<User> list = searchHelperService.searchUserByInterestedEvents(events);
        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    public void assertSearchUserByInterestedEventsIsWorkingWithNullSet() {

        Assertions.assertThrows(NotFoundException.class, () -> searchHelperService.searchUserByInterestedEvents(null));

    }

    @Test
    public void assertSearchUserInterestCategoriesWithInvalidArgument() {

        Assertions.assertThrows(NotFoundException.class, () -> searchHelperService.searchUserInterestCategories(100L));
    }

    @Test
    public void assertSearchUserInterestCategoriesWithNullArgument() {

        Assertions.assertThrows(NotFoundException.class, () -> searchHelperService.searchUserInterestCategories(null));
    }


    @Test
    public void assertSearchUserInterestCategoriesWithNoUserEvents() {
        User user1 = TestsRepositoryUtil.createUser("login100", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        Assertions.assertThrows(NotFoundException.class, () -> searchHelperService.searchUserInterestCategories(user1.getId()));
    }

    @Test
    public void assertSearchUserInterestCategoriesWithValidData() throws NotFoundException {
        Set<Category> set = new HashSet<>();
        set.add(category);
        Category category2 = TestsRepositoryUtil.createCategory("cat", "ss");
        set.add(category2);
        Event event2 = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", true, address, user, set);
        eventRepository.save(event2);
        user.getEvents().add(event2);
        userRepository.save(user);
        List<Category> categories = searchHelperService.searchUserInterestCategories(user.getId());
        assertThat(categories.size()).isEqualTo(2);
    }

}
