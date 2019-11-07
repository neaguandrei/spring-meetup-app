package com.cegeka.academy.service.event;

import com.cegeka.academy.domain.Event;
import com.cegeka.academy.domain.User;
import com.cegeka.academy.service.dto.EventDTO;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventService {

    public List<Event> getAllPubicEvents();

    List<EventDTO> getAllByOwner(User owner) throws NotFoundException;

    Optional<Event> getEvent(Long id);

    void createEvent(Event event);

    void updateEvent(Event event);

    void deleteEventById(Long id);

    void addUserToPublicEvent(Long eventId, Long userId) throws NotFoundException;

    List<EventDTO> getEventsByUser(Long userId) throws NotFoundException;

    List<EventDTO> getEventsByUserInterestedCategories(Long userId) throws NotFoundException;

    void uploadEventCoverPhoto(Long eventId, MultipartFile image) throws NotFoundException, IOException;

    List<EventDTO> getEventsByName(String eventName) throws NotFoundException;

    List<EventDTO> getEventsByDates(Date startDate, Date endDate) throws NotFoundException;
}
