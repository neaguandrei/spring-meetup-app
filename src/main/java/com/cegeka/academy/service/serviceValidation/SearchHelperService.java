package com.cegeka.academy.service.serviceValidation;

import com.cegeka.academy.domain.Category;
import com.cegeka.academy.domain.Event;
import com.cegeka.academy.domain.User;
import com.cegeka.academy.repository.CategoryRepository;
import com.cegeka.academy.repository.EventRepository;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class SearchHelperService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;


    @Autowired
    public SearchHelperService(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    public Set<Event> searchEventsByCategoryName(String categoryName) throws NotFoundException {
        Category category = categoryRepository.findByName(categoryName);

        if (category == null || category.getEvents().size() == 0) {
            throw new NotFoundException().setMessage("Category or eventCategory not found");
        }
        return category.getEvents();
    }

    public List<User> searchUserByInterestedEvents(Set<Event> events) throws NotFoundException {

        if (events == null) {
            throw new NotFoundException().setMessage("Events not found");
        }

        return events.stream().filter(event -> event.getUsers().size() > 0).
                flatMap(event -> event.getUsers().stream()).collect(Collectors.toList());

    }

    public List<Category> searchUserInterestCategories(Long userId) throws NotFoundException {

        List<Event> userEvents = eventRepository.findByUsers_id(userId);
        if (userEvents == null || userEvents.isEmpty()) {
            throw new NotFoundException().setMessage("User events not found");
        }

        return categoryRepository.findDistinctByEventsIn(userEvents);
    }
}
