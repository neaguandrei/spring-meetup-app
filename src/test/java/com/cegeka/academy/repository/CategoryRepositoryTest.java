package com.cegeka.academy.repository;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.Address;
import com.cegeka.academy.domain.Category;
import com.cegeka.academy.domain.Event;
import com.cegeka.academy.domain.User;
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
public class CategoryRepositoryTest {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Event event, event1;

    @BeforeEach
    public void init() {
        eventRepository.deleteAll();
        categoryRepository.deleteAll();
        User user = TestsRepositoryUtil.createUser("login", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        userRepository.save(user);
        Address address = TestsRepositoryUtil.createAddress("Romania", "Bucuresti", "Splai", "333", "Casa", "Casa magica");
        addressRepository.saveAndFlush(address);
        Category category_1 = TestsRepositoryUtil.createCategory("Sport", "Liber pentru toate varstele!");
        Category category_3 = TestsRepositoryUtil.createCategory("Arta", "Expozitii de arta");
        categoryRepository.save(category_1);
        categoryRepository.save(category_3);
        Set<Category> list1 = new HashSet<>();
        list1.add(category_1);
        list1.add(category_3);
        event = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", true, address, user, list1);
        eventRepository.save(event);
        Category category1 = TestsRepositoryUtil.createCategory("Ana", "description1");
        categoryRepository.save(category1);
        categoryRepository.saveAndFlush(category1);
        event.getCategories().add(category1);
        eventRepository.save(event);
        Category category2 = TestsRepositoryUtil.createCategory("Dana", "description2");
        categoryRepository.save(category2);
        eventRepository.saveAndFlush(event);
        categoryRepository.saveAndFlush(category2);
        event.getCategories().add(category2);
        eventRepository.save(event);
        Category category3 = TestsRepositoryUtil.createCategory("Doriana", "description");
        categoryRepository.save(category3);
        event.getCategories().add(category3);
        eventRepository.save(event);
        Set<Category> list2 = new HashSet<>();
        list2.add(category1);
        list2.add(category2);
        event1 = TestsRepositoryUtil.createEvent("Ana are mere", "Krushers Party", true, address, user, list2);
        eventRepository.save(event1);

    }

    @Test
    public void testFindAllByEventId() {

        List<Category> categories = categoryRepository.findAllByEvents_id(event.getId());
        assertThat(categories.size()).isEqualTo(5);

    }

    @Test
    public void assertThatFindByNameIsWorkingWithValidArgument() {

        Category findCategory = categoryRepository.findByName("Ana");
        assertThat(findCategory).isEqualTo(categoryRepository.findAll().get(2));

    }

    @Test
    public void assertThatFindByNameIsWorkingWithInvalidArgument() {

        Category findCategory = categoryRepository.findByName("Ana1");
        assertThat(findCategory).isEqualTo(null);
    }

    @Test
    public void assertThatFindByNameIsWorkingWithNullArgument() {

        Category findCategory = categoryRepository.findByName(null);
        assertThat(findCategory).isEqualTo(null);
    }

    @Test
    public void assertThatFindDistinctByEventsIsWorking() {

        List<Event> events = new ArrayList<>();
        events.add(event);
        events.add(event1);
        List<Category> categories = categoryRepository.findDistinctByEventsIn(events);
        assertThat(categories.size()).isEqualTo(5);

    }
}
