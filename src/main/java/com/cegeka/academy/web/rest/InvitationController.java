package com.cegeka.academy.web.rest;

import com.cegeka.academy.domain.Invitation;
import com.cegeka.academy.repository.InvitationRepository;
import com.cegeka.academy.service.dto.InvitationDTO;
import com.cegeka.academy.service.invitation.InvitationService;
import com.cegeka.academy.service.serviceValidation.ValidationAccessService;
import com.cegeka.academy.web.rest.errors.ExistingItemException;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import com.cegeka.academy.web.rest.errors.UnauthorizedUserException;
import com.cegeka.academy.web.rest.strategy.AcceptInvitationStrategy;
import com.cegeka.academy.web.rest.strategy.InvitationConstants;
import com.cegeka.academy.web.rest.strategy.InvitationStatusContext;
import com.cegeka.academy.web.rest.strategy.RejectInvitationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("invitation")
public class InvitationController {

    private final InvitationService invitationService;

    private final InvitationRepository invitationRepository;

    private final ValidationAccessService validationAccessService;

    private final InvitationStatusContext invitationStatusContext;

    @Autowired
    public InvitationController(InvitationService invitationService, ValidationAccessService validationAccessService,
                                InvitationRepository invitationRepository, InvitationStatusContext invitationStatusContext) {
        this.invitationService = invitationService;
        this.validationAccessService = validationAccessService;
        this.invitationRepository = invitationRepository;
        this.invitationStatusContext = invitationStatusContext;
    }

    @GetMapping("/all")
    public List<InvitationDTO> getAllInvitations(){

        return invitationService.getAllInvitations();
    }

    @PostMapping
    public void saveInvitation(@Valid @RequestBody Invitation newInvitation) {

         invitationService.saveInvitation(newInvitation);
    }

    @PutMapping
    public void replaceInvitation(@RequestBody Invitation newInvitation) throws NotFoundException {

        Optional<Invitation> updateInvitation = invitationRepository.findById(newInvitation.getId());

        if (updateInvitation.isPresent()) {

            if (validationAccessService.verifyUserAccessForInvitationEntity(newInvitation.getId())) {

                invitationService.updateInvitation(newInvitation);

            } else {

                throw new UnauthorizedUserException().setMessage("You have no right to update this invitation");
            }

        } else {

            throw new NotFoundException().setMessage("Invitation not found");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteInvitation(@PathVariable Long id) throws NotFoundException {

        Optional<Invitation> deleteInvitation = invitationRepository.findById(id);

        if (deleteInvitation.isPresent()) {

            if (validationAccessService.verifyUserAccessForInvitationEntity(id)) {

                invitationService.deleteInvitationById(id);

            } else {

                throw new UnauthorizedUserException().setMessage("You have no right to delete this invitation");
            }
        } else {

            throw new NotFoundException().setMessage("Invitation not found");
        }
    }

    @GetMapping("/pending/{userId}")
    public List<InvitationDTO> getPendingInvitationsByUserId(@PathVariable Long userId) {

        return invitationService.getPendingInvitationsByUserId(userId);
    }

    @PutMapping("/{decide}/{id}")
    public void decideAboutInvitationStatus(@PathVariable String decide, @PathVariable Long id) throws NotFoundException {

        if (validationAccessService.verifyUserAccessForInvitationEntity(id)) {

            switch (decide) {
                case InvitationConstants.ACCEPT_INVITATION:
                    invitationStatusContext.setInvitationStrategy(new AcceptInvitationStrategy());
                    invitationStatusContext.execute(id);
                    break;
                case InvitationConstants.REJECT_INVITATION:
                    invitationStatusContext.setInvitationStrategy(new RejectInvitationStrategy());
                    invitationStatusContext.execute(id);
                    break;
                default:
                    throw new NotFoundException();
            }
        } else {

            throw new UnauthorizedUserException().setMessage("You have no rights to change the status of this invitation.");
        }
    }

    @PostMapping("/send/{idGroup}")
    public void sendGroupInvitationsToPrivateEvents(@PathVariable Long idGroup,
                                                    @Valid @RequestBody Invitation newInvitation) throws NotFoundException {

        invitationService.sendGroupInvitationsToPrivateEvents(idGroup, newInvitation);
    }

    @PostMapping("/challenge-invitation/{challengeId}")
    public Invitation createChallengeInvitationForOneUser(@RequestBody InvitationDTO invitationDTO,
                                                    @PathVariable Long challengeId) throws NotFoundException, ExistingItemException {

        return invitationService.createChallengeInvitationForOneUser(invitationDTO, challengeId);
    }

    @GetMapping("/{invitationId}")
    public Invitation getInvitation(@PathVariable Long invitationId) throws NotFoundException {

        return invitationService.getInvitation(invitationId);
    }
}
