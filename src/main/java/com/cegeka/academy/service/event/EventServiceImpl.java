package com.cegeka.academy.service.event;

import com.cegeka.academy.domain.Event;
import com.cegeka.academy.domain.User;
import com.cegeka.academy.domain.UserChallenge;
import com.cegeka.academy.repository.EventRepository;
import com.cegeka.academy.repository.UserRepository;
import com.cegeka.academy.service.UserService;
import com.cegeka.academy.service.dto.EventDTO;
import com.cegeka.academy.service.mapper.EventMapper;
import com.cegeka.academy.service.serviceValidation.SearchHelperService;
import com.cegeka.academy.service.util.SortUtil;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final SearchHelperService searchService;

    private Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserService userService, UserRepository userRepository, SearchHelperService searchService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.searchService = searchService;
    }

    private List<EventDTO> filterEvents(List<Event> events) throws NotFoundException {
        if (events == null || events.isEmpty()) {
            throw new NotFoundException().setMessage("No events found");
        }
        ArrayList<EventDTO> eventDTOS = new ArrayList<>();
        Optional<User> currentUser = userService.getUserWithAuthorities();
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            for (Event event : events) {
                if (!userRepository.findAllByEvents_id(event.getId()).contains(user)) {
                    EventDTO aux = EventMapper.convertEventtoEventDTO(event);
                    eventDTOS.add(aux);
                }
            }
            return SortUtil.sortEventsByStartDate(eventDTOS);
        } else return null;
    }

    public List<Event> getAllPubicEvents() {
        return eventRepository.findAllByPublicEventIsTrue();
    }

    public List<EventDTO> getAllByOwner(User owner) throws NotFoundException {

        List<EventDTO> ownerEvents = new ArrayList<>();
        List<Event> events = eventRepository.findAllByOwner(owner);
        if (events == null || events.isEmpty()) {
            throw new NotFoundException().setMessage("No events found");
        }
        for (Event event : events) {
            EventDTO aux = EventMapper.convertEventtoEventDTO(event);
            ownerEvents.add(aux);
        }

        return ownerEvents;
    }

    @Override
    public Optional<Event> getEvent(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public void createEvent(Event event) {
        if (userService.getUserWithAuthorities().isPresent()) {
            User currentUser = userService.getUserWithAuthorities().get();
            event.setOwner(currentUser);
            logger.info("Event with id: " + eventRepository.save(event).getId() + "  was saved to database.");
        }
        //TODO:replace that
        else {
            logger.info("Event with id: " + eventRepository.save(event).getId() + "  was saved to database.");
        }

    }

    @Override
    public void updateEvent(Event event) {
        logger.info("Event with id: " + eventRepository.save(event).getId() + "  was updated into database.");
    }

    @Override
    public void deleteEventById(Long id) {
        eventRepository.findById(id).ifPresent(eventRepository::delete);
    }

    @Override
    public void addUserToPublicEvent(Long eventId, Long userId) throws NotFoundException {
        Event event = eventRepository.findById(eventId).
                orElseThrow(() -> new NotFoundException().setMessage("Event not found"));
        if (event.isPublicEvent()) {
            User user = userRepository.findById(userId).
                    orElseThrow(() -> new NotFoundException().setMessage("User not found"));
            user.getEvents().add(event);
            logger.info("Event with id: " + event.getId() + " has a new user with id " + userRepository.save(user).getId());

        }
    }

    @Override
    public List<EventDTO> getEventsByUser(Long userId) throws NotFoundException {
        List<EventDTO> userEvents = new ArrayList<>();
        List<Event> events = eventRepository.findByUsers_id(userId);

        if (events == null || events.isEmpty()) {
            throw new NotFoundException().setMessage("No events found");
        }
        for (Event event : events) {
            EventDTO aux = EventMapper.convertEventtoEventDTO(event);
            userEvents.add(aux);
        }

        return userEvents;

    }

    @Override
    public List<EventDTO> getEventsByUserInterestedCategories(Long userId) throws NotFoundException {

        List<Event> events = eventRepository.findDistinctByPublicEventIsTrueAndCategoriesIn(searchService.searchUserInterestCategories(userId));
        return filterEvents(events);

    }

    @Override
    public List<EventDTO> getEventsByName(String eventName) throws NotFoundException {
        List<Event> events = eventRepository.findAllByPublicEventIsTrueAndNameContaining(eventName);
        return filterEvents(events);
    }

    @Override
    public List<EventDTO> getEventsByDates(Date startDate, Date endDate) throws NotFoundException {
        List<Event> events = eventRepository.findAllByPublicEventIsTrueAndStartDateIsBetween(startDate, endDate);
        return filterEvents(events);
    }

    @Override
    public void uploadEventCoverPhoto(Long eventId, MultipartFile image) throws NotFoundException, IOException {
        Event event=eventRepository.findById(eventId).orElseThrow(
                ()->new NotFoundException().setMessage("Event not found"));
        event.setCoverPhoto(image.getBytes());
        eventRepository.save(event);
    }
}
