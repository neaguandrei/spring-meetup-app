package com.cegeka.academy.service.invitation;

import com.cegeka.academy.domain.*;
import com.cegeka.academy.domain.enums.InvitationStatus;
import com.cegeka.academy.repository.*;
import com.cegeka.academy.service.dto.InvitationDTO;
import com.cegeka.academy.service.mapper.InvitationMapper;
import com.cegeka.academy.service.serviceValidation.CheckUniqueService;
import com.cegeka.academy.service.userChallenge.UserChallengeService;
import com.cegeka.academy.web.rest.errors.ExistingItemException;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final EventRepository eventRepository;
    private final GroupUserRoleRepository groupUserRoleRepository;
    private final UserRepository userRepository;
    private final UserChallengeService userChallengeService;
    private final ChallengeRepository challengeRepository;
    private final CheckUniqueService checkUniqueService;
    private final UserChallengeRepository userChallengeRepository;


    private Logger logger = LoggerFactory.getLogger(InvitationServiceImpl.class);

    @Autowired
    public InvitationServiceImpl(InvitationRepository invitationRepository, EventRepository eventRepository,
                                 GroupUserRoleRepository groupUserRoleRepository, UserRepository userRepository,
                                 CheckUniqueService checkUniqueService, UserChallengeService userChallengeService,
                                 ChallengeRepository challengeRepository, UserChallengeRepository userChallengeRepository) {
        this.invitationRepository = invitationRepository;
        this.eventRepository = eventRepository;
        this.groupUserRoleRepository = groupUserRoleRepository;
        this.userRepository = userRepository;
        this.checkUniqueService = checkUniqueService;
        this.userChallengeService = userChallengeService;
        this.challengeRepository = challengeRepository;
        this.userChallengeRepository = userChallengeRepository;
    }

    @Override
    public List<InvitationDTO> getAllInvitations() {

        List<InvitationDTO> listToShow = new ArrayList<>();
        List<Invitation> list = invitationRepository.findAll();
        for (Invitation invitation : list) {
            InvitationDTO aux = InvitationMapper.convertInvitationEntityToInvitationDTO(invitation);
            listToShow.add(aux);
        }

        return listToShow;
    }

    @Override
    public void saveInvitation(Invitation invitation) {

        invitation.setStatus(InvitationStatus.PENDING.name());
        logger.info("Invitation with id: " + invitationRepository.save(invitation).getId() + "  was saved to database.");
        eventRepository.findById(invitation.getEvent().getId()).ifPresent(event -> {
            event.getPendingInvitations().add(invitation);
            eventRepository.save(event);
        });
    }

    @Override
    public void updateInvitation(Invitation invitation) {

        logger.info("Invitation with id: " + invitationRepository.save(invitation).getId() + "  was updated into database.");

    }

    @Override
    public void deleteInvitationById(Long id) {

        invitationRepository.findById(id).ifPresent(invitation -> invitationRepository.delete(invitation));
    }

    @Override
    public List<InvitationDTO> getPendingInvitationsByUserId(Long userId) {

        List<InvitationDTO> pendingInvitations = new ArrayList<>();
        List<Invitation> list = invitationRepository.findByUser_IdAndStatusIgnoreCase(userId, InvitationStatus.PENDING.name());
        for (Invitation invitation : list) {
            InvitationDTO aux = InvitationMapper.convertInvitationEntityToInvitationDTO(invitation);
            pendingInvitations.add(aux);
        }

        return pendingInvitations;
    }

    @Override
    public void acceptInvitation(Long invitationId) throws NotFoundException {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException().setMessage("Invitation not found"));
        invitation.setStatus(InvitationStatus.ACCEPTED.name());
        logger.info("Invitation with id: " + invitationRepository.save(invitation).getId() + "  was accepted by the user.");

        Event event = eventRepository.findById(invitation.getEvent().getId()).
                orElseThrow(() -> new NotFoundException().setMessage("Event not found"));
        event.getPendingInvitations().remove(invitation);
        eventRepository.save(event);

        User user = userRepository.findById(invitation.getUser().getId())
                .orElseThrow(() -> new NotFoundException().setMessage("User not found"));
        user.getEvents().add(event);
        userRepository.save(user);

    }

    @Override
    public void rejectInvitation(Long invitationId) throws NotFoundException {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException().setMessage("Invitation not found"));
        invitation.setStatus(InvitationStatus.REJECTED.name());
        logger.info("Invitation with id: " + invitationRepository.save(invitation).getId() + "  was rejected by the user.");

    }

    @Override
    public void sendGroupInvitationsToPrivateEvents(Long idGroup, Invitation invitation) throws NotFoundException {

        if (invitation.getEvent() == null) {
            throw new NotFoundException().setMessage("Event not found");

        }
        if (!invitation.getEvent().isPublicEvent()) {

            List<GroupUserRole> listIdUsers = groupUserRoleRepository.findAllByGroupId(idGroup);

            for (GroupUserRole userGroup : listIdUsers) {

                Optional<User> user = userRepository.findById(userGroup.getUser().getId());
                Optional<Event> event = eventRepository.findById(invitation.getEvent().getId());

                user.orElseThrow(() -> new NotFoundException().setMessage("User not found"));
                event.orElseThrow(() -> new NotFoundException().setMessage("Event not found"));

                if (checkUniqueService.checkUniqueInvitation(user.get(), event.get())) {

                    Invitation invitationSendToGroup = InvitationMapper.createInvitation(invitation.getDescription(), invitation.getStatus(), user.get(), invitation.getEvent());
                    invitationRepository.save(invitationSendToGroup);
                }
            }
        }

    }

    @Override
    public void sendInvitationForPrivateEventsToUserList(List<User> userList, Invitation invitation) throws NotFoundException {
        if (invitation.getEvent() == null) {
            throw new NotFoundException().setMessage("Event not found");
        }
        if (!invitation.getEvent().isPublicEvent()) {
            for (User userInvitation : userList) {
                Optional<Event> event = eventRepository.findById(invitation.getEvent().getId());
                Optional<User> user = userRepository.findById(userInvitation.getId());
                event.orElseThrow(() -> new NotFoundException().setMessage("Event not found"));
                user.orElseThrow(() -> new NotFoundException().setMessage("User not found"));
                if (checkUniqueService.checkUniqueInvitation(user.get(), event.get())) {
                    Invitation invitationForUserList = InvitationMapper.createInvitation(invitation.getDescription(), invitation.getStatus(), user.get(), event.get());
                    invitationRepository.save(invitationForUserList);
                }
            }
        }
    }

    @Override
    public Invitation createChallengeInvitationForOneUser(InvitationDTO invitationDTO, Long challengeId)
            throws ExistingItemException, NotFoundException {

        long userId = invitationDTO.getUserId();
        Optional<UserChallenge> userChallengeOptional = userChallengeRepository.findByUserIdAndChallengeId(userId, challengeId);

        if (userChallengeOptional.isPresent()) {
            throw new ExistingItemException();
        } else {

            User user = userRepository.findById(userId).orElseThrow(
                    () -> new NotFoundException().setMessage("User not found"));
            Invitation invitation = InvitationMapper.createInvitation(
                    invitationDTO.getDescription(),
                    invitationDTO.getStatus(),
                    user,
                    null);
            invitationRepository.save(invitation);
            Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                    () -> new NotFoundException().setMessage("Challenge not found"));

            userChallengeService.initUserChallenge(challenge, invitation);

            return invitation;
        }
    }

    @Override
    public Invitation getInvitation(Long invitationId) throws NotFoundException {
        return invitationRepository.findById(invitationId).orElseThrow(() -> new NotFoundException()
                .setMessage("Invitation not found"));
    }


}
