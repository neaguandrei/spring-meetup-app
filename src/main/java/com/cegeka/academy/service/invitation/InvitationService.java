package com.cegeka.academy.service.invitation;

import com.cegeka.academy.domain.Invitation;
import com.cegeka.academy.domain.User;
import com.cegeka.academy.service.dto.InvitationDTO;
import com.cegeka.academy.web.rest.errors.ExistingItemException;
import com.cegeka.academy.web.rest.errors.NotFoundException;

import java.util.List;

public interface InvitationService {

    List<InvitationDTO> getAllInvitations();
    void saveInvitation(Invitation invitation);
    void updateInvitation(Invitation invitation);
    void deleteInvitationById(Long id);

    List<InvitationDTO> getPendingInvitationsByUserId(Long userId);

    void acceptInvitation(Long invitationId) throws NotFoundException;

    void rejectInvitation(Long invitationId) throws NotFoundException;

    void sendGroupInvitationsToPrivateEvents(Long idGroup, Invitation invitation) throws NotFoundException;

    void sendInvitationForPrivateEventsToUserList(List<User> userList, Invitation invitation) throws NotFoundException;

    Invitation createChallengeInvitationForOneUser (InvitationDTO invitationDTO, Long challengeId) throws ExistingItemException, NotFoundException;

    Invitation getInvitation(Long invitationId) throws NotFoundException;

}
