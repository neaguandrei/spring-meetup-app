package com.cegeka.academy.service;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.*;
import com.cegeka.academy.domain.enums.InvitationStatus;
import com.cegeka.academy.repository.*;
import com.cegeka.academy.repository.util.TestsRepositoryUtil;
import com.cegeka.academy.service.invitation.InvitationService;
import com.cegeka.academy.service.serviceValidation.CheckUniqueService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest(classes = AcademyProjectApp.class)
@Transactional
public class CheckUniqueServiceTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CheckUniqueService checkUniqueService;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User user, uninvitedUser;
    private Invitation invitation;
    private Address address;
    private Event event, newEvent;

    @BeforeEach
    public void init() {

        user = TestsRepositoryUtil.createUser("login", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        uninvitedUser = TestsRepositoryUtil.createUser("login1", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        userRepository.save(user);
        userRepository.save(uninvitedUser);
        address = TestsRepositoryUtil.createAddress("Romania", "Bucuresti", "Splai", "333", "Casa", "Casa magica");
        addressRepository.saveAndFlush(address);
        Category category1 = TestsRepositoryUtil.createCategory("Sport", "Liber pentru toate varstele!");
        Category category3 = TestsRepositoryUtil.createCategory("Arta", "Expozitii de arta");
        categoryRepository.save(category1);
        categoryRepository.save(category3);
        Set<Category> list1 = new HashSet<>();
        list1.add(category1);
        list1.add(category3);
        event = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", false, address, user, list1);
        eventRepository.saveAndFlush(event);
        newEvent = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", false, address, user, list1);
        eventRepository.saveAndFlush(newEvent);
        invitation = TestsRepositoryUtil.createInvitation(InvitationStatus.PENDING.name(), "ana are mere", event, user);
        invitationService.saveInvitation(invitation);
    }

    @Test
    public void assertThatCheckUniqueServiceIsWorkingWithDifferentUserAndSameEvent() {
        Assert.assertTrue(checkUniqueService.checkUniqueInvitation(uninvitedUser, event));
    }

    @Test
    public void assertThatCheckUniqueServiceIsWorkingWithDifferentEventAndSameUser() {
        Assert.assertTrue(checkUniqueService.checkUniqueInvitation(user, newEvent));
    }

    @Test
    public void assertThatCheckUniqueServiceIsWorkingWithDifferentEventAndDifferentUser() {
        Assert.assertTrue(checkUniqueService.checkUniqueInvitation(uninvitedUser, newEvent));
    }

    @Test
    public void assertThatCheckUniqueServiceIsWorkingWithSameEventAndUser() {
        Assert.assertFalse(checkUniqueService.checkUniqueInvitation(user, event));
    }

    @Test
    public void assertThatCheckUniqueServiceIsWorkingWithNullEventOnValidation() {
        Invitation newInvitation = TestsRepositoryUtil.createInvitation(InvitationStatus.ACCEPTED.name(), "aaa", null, user);
        newInvitation.setId(invitationRepository.findAll().get(0).getId());
        invitationService.updateInvitation(newInvitation);
        System.out.println(invitationRepository.findAll().get(0).toString());
        Assert.assertTrue(checkUniqueService.checkUniqueInvitation(user, event));
    }

    @Test
    public void assertThatCheckUniqueServiceIsWorkingWithNullUserAndEventOnValidation() {
        Invitation newInvitation = TestsRepositoryUtil.createInvitation(InvitationStatus.ACCEPTED.name(), "aaa", null, null);
        newInvitation.setId(invitationRepository.findAll().get(0).getId());
        invitationService.updateInvitation(newInvitation);
        System.out.println(invitationRepository.findAll().get(0).toString());
        Assert.assertTrue(checkUniqueService.checkUniqueInvitation(user, event));
    }
}
