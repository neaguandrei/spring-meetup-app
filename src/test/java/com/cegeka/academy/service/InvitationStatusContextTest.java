package com.cegeka.academy.service;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.*;
import com.cegeka.academy.domain.enums.InvitationStatus;
import com.cegeka.academy.repository.AddressRepository;
import com.cegeka.academy.repository.EventRepository;
import com.cegeka.academy.repository.InvitationRepository;
import com.cegeka.academy.repository.UserRepository;
import com.cegeka.academy.repository.util.TestsRepositoryUtil;
import com.cegeka.academy.service.invitation.InvitationService;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import com.cegeka.academy.web.rest.strategy.AcceptInvitationStrategy;
import com.cegeka.academy.web.rest.strategy.InvitationStatusContext;
import com.cegeka.academy.web.rest.strategy.RejectInvitationStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AcademyProjectApp.class)
@Transactional
public class InvitationStatusContextTest {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private InvitationStatusContext invitationStatusContext;

    @BeforeEach
    public void init() {
        eventRepository.deleteAll();
        invitationRepository.deleteAll();
        addressRepository.deleteAll();
        userRepository.deleteAll();
        Address address = TestsRepositoryUtil.createAddress("Romania", "Bucuresti", "Splai", "333", "Casa", "Casa magica");
        addressRepository.save(address);
        User user = TestsRepositoryUtil.createUser("login", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        userRepository.save(user);
        Set<Category> list1 = new HashSet<>();
        Event event = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", false, address, user, list1);
        eventRepository.save(event);
        Invitation invitation = TestsRepositoryUtil.createInvitation(InvitationStatus.PENDING.name(), "ana are mere", event, user);
        invitationService.saveInvitation(invitation);

    }

    @Test
    public void assertThatAcceptInvitationIsWorking() throws NotFoundException {

        invitationStatusContext.setInvitationStrategy(new AcceptInvitationStrategy());
        invitationStatusContext.execute(invitationRepository.findAll().get(0).getId());

        assertThat(invitationRepository.findAll().get(0).getStatus()).isEqualTo(InvitationStatus.ACCEPTED.toString());
    }


    @Test
    public void assertThatRejectInvitationIsWorking() throws NotFoundException {

        invitationStatusContext.setInvitationStrategy(new RejectInvitationStrategy());
        invitationStatusContext.execute(invitationRepository.findAll().get(0).getId());

        assertThat(invitationRepository.findAll().get(0).getStatus()).isEqualTo(InvitationStatus.REJECTED.toString());
    }

    @Test
    public void assertThatAcceptInvitationIsWorkingWithInvalidId() {

        invitationStatusContext.setInvitationStrategy(new AcceptInvitationStrategy());
        Assertions.assertThrows(NotFoundException.class, () -> invitationStatusContext.execute(1000L));
    }

    @Test
    public void assertThatRejectInvitationIsWorkingWithInvalidId() {

        invitationStatusContext.setInvitationStrategy(new RejectInvitationStrategy());
        Assertions.assertThrows(NotFoundException.class, () -> invitationStatusContext.execute(1000L));
    }

}
