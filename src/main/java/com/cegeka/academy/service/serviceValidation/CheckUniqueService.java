package com.cegeka.academy.service.serviceValidation;

import com.cegeka.academy.domain.Event;
import com.cegeka.academy.domain.Invitation;
import com.cegeka.academy.domain.User;
import com.cegeka.academy.repository.InvitationRepository;
import com.cegeka.academy.service.invitation.InvitationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class CheckUniqueService {

    private Logger logger = LoggerFactory.getLogger(InvitationServiceImpl.class);

    private final InvitationRepository invitationRepository;

    @Autowired
    public CheckUniqueService(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    public boolean checkUniqueInvitation(User user, Event event) {

        List<Invitation> allInvitations = invitationRepository.findAll();

        for (Invitation findInvitation : allInvitations) {

            Optional<User> findUser = Optional.ofNullable(findInvitation.getUser());
            Optional<Event> findEvent = Optional.ofNullable(findInvitation.getEvent());

            if (!findUser.isPresent() || !findEvent.isPresent()) {

                return true;
            }
            if (findUser.get().equals(user) && findEvent.get().equals(event)) {

                logger.info("This user has already been invited to this event");

                return false;
            }
        }

        return true;
    }
}
