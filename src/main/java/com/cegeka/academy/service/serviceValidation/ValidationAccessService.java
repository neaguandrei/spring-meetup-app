package com.cegeka.academy.service.serviceValidation;

import com.cegeka.academy.domain.Event;
import com.cegeka.academy.domain.Invitation;
import com.cegeka.academy.domain.User;
import com.cegeka.academy.repository.EventRepository;
import com.cegeka.academy.repository.InvitationRepository;
import com.cegeka.academy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
public class ValidationAccessService {

    private final UserService userService;
    private final InvitationRepository invitationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public ValidationAccessService(UserService userService, InvitationRepository invitationRepository, EventRepository eventRepository) {
        this.userService = userService;
        this.invitationRepository = invitationRepository;
        this.eventRepository = eventRepository;
    }

    public boolean verifyUserAccessForInvitationEntity(Long invitationId){

        if(invitationId == null){

            return false;

        }else {

            Optional<User> user = userService.getUserWithAuthorities();

            if (user.isPresent()) {

                User userLogged = user.get();

                Optional<Invitation> invitation = invitationRepository.findById(invitationId);

                if(invitation.isPresent()) {

                    User invitedUser = invitation.get().getUser();

                    if (invitedUser == null || userLogged == null || userLogged.getId() != invitedUser.getId()) {

                        return false;
                    }
                }

            }else{

                return false;
            }

          }

        return true;
    }

    public boolean verifyUserAccessForEventEntity(Long eventId) {
        if (eventId == null) {

            return false;
        }

        Optional<User> user = userService.getUserWithAuthorities();

        if (user.isPresent()) {

            User userLogged = user.get();

            Optional<Event> event = eventRepository.findById(eventId);

            if (event.isPresent()) {

                User eventOwner = event.get().getOwner();

                return eventOwner != null && userLogged.getId() == eventOwner.getId();
            }

        } else {

            return false;
        }
        return true;
    }
}
