package com.cegeka.academy.web.rest;

import com.cegeka.academy.domain.Event;
import com.cegeka.academy.domain.User;
import com.cegeka.academy.repository.EventRepository;
import com.cegeka.academy.repository.UserRepository;
import com.cegeka.academy.service.UserService;
import com.cegeka.academy.service.dto.EventDTO;
import com.cegeka.academy.service.event.EventService;
import com.cegeka.academy.service.serviceValidation.ExpirationCheckService;
import com.cegeka.academy.service.serviceValidation.ValidationAccessService;
import com.cegeka.academy.web.rest.errors.ExpiredObjectException;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import com.cegeka.academy.web.rest.errors.UnauthorizedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("event")
public class EventController {

    private final EventService eventService;
    private final ValidationAccessService validationAccessService;
    private final ExpirationCheckService expirationCheckService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public EventController(EventService eventService, ValidationAccessService validationAccessService,
                           ExpirationCheckService expirationCheckService, EventRepository eventRepository,
                           UserRepository userRepository, UserService userService) {
        this.eventService = eventService;
        this.validationAccessService = validationAccessService;
        this.expirationCheckService = expirationCheckService;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/all_public")
    public List<Event> getAllPublicEvents() {
        return eventService.getAllPubicEvents();
    }

    @PostMapping
    public void createEvent(@Valid @RequestBody Event event) {

        eventService.createEvent(event);
    }

    @PutMapping
    public void updateEvent(@RequestBody Event event) throws NotFoundException {

        Optional<Event> eventUpdate = eventRepository.findById(event.getId());

        if (eventUpdate.isPresent()) {

            if (validationAccessService.verifyUserAccessForEventEntity(event.getId()) && expirationCheckService.availabilityCheck(event.getEndDate())) {

                eventService.updateEvent(event);

            } else if (!validationAccessService.verifyUserAccessForEventEntity(event.getId())) {

                throw new UnauthorizedUserException().setMessage("You have no right to update this event");

            } else if (!expirationCheckService.availabilityCheck(event.getEndDate())) {

                throw new ExpiredObjectException().setMessage("This event is expired");

            }
        } else {

            throw new NotFoundException().setMessage("Event not found");
        }

    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) throws NotFoundException {

        Optional<Event> event = eventRepository.findById(id);

        if (event.isPresent()) {

            if (validationAccessService.verifyUserAccessForEventEntity(id)) {

                eventService.deleteEventById(id);

            } else {

                throw new UnauthorizedUserException().setMessage("You have no right to delete this event");

            }
        } else {

            throw new NotFoundException().setMessage("Event not found");
        }

    }

    @PutMapping("user/{eventId}/{userId}")
    public void addUserToPublicEvent(@PathVariable(value = "eventId") Long eventId, @PathVariable(value = "userId") Long userId) throws NotFoundException {

        eventService.addUserToPublicEvent(eventId, userId);

    }

    @GetMapping("/all/category/{id}")
    public List<Event> getAllCategoryEvents(@PathVariable(value = "id") Long id) {
        return eventRepository.findAllByCategories_id(id);
    }
    @GetMapping("/user/{id}")
    public List<EventDTO> getEventsByUser(@PathVariable(value = "id") Long id) throws NotFoundException {
        return eventService.getEventsByUser(id);
    }

    @GetMapping("/owner/{id}")
    public List<EventDTO> getEventsByOwner(@PathVariable(value = "id") Long id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        user.orElseThrow(() -> new NotFoundException().setMessage("User not found"));
        return eventService.getAllByOwner(user.get());
    }

    @GetMapping("/interest")
    public List<EventDTO> getEventsByUserInterest() throws NotFoundException {

        User currentUser = userService.getUserWithAuthorities().orElseThrow(() -> new NotFoundException().setMessage("User not found"));
        return eventService.getEventsByUserInterestedCategories(currentUser.getId());
    }

    @PostMapping(value = "/photo/{eventId}")
    public ResponseEntity<String> uploadCoverPhoto(@PathVariable("eventId") Long eventId,
                                                    @RequestParam("image") MultipartFile image) throws IOException, NotFoundException {

        eventService.uploadEventCoverPhoto(eventId, image);

        return ResponseEntity.ok("Cover has been uploaded.");
    }

}
