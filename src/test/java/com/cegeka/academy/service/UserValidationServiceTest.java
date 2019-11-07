package com.cegeka.academy.service;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.*;
import com.cegeka.academy.repository.*;
import com.cegeka.academy.repository.util.TestsRepositoryUtil;
import com.cegeka.academy.service.invitation.InvitationService;
import com.cegeka.academy.service.serviceValidation.ValidationAccessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AcademyProjectApp.class)
@Transactional
public class UserValidationServiceTest {

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationAccessService validationAccessService;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void init() {

        User user = TestsRepositoryUtil.createUser("login", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        userRepository.save(user);
        Address address = TestsRepositoryUtil.createAddress("Romania", "Bucuresti", "Splai", "333", "Casa", "Casa magica");
        addressRepository.saveAndFlush(address);
        Category category1 = TestsRepositoryUtil.createCategory("Sport", "Liber pentru toate varstele!");
        Category category3 = TestsRepositoryUtil.createCategory("Arta", "Expozitii de arta");
        categoryRepository.save(category1);
        categoryRepository.save(category3);
        Set<Category> list1 = new HashSet<>();
        list1.add(category1);
        list1.add(category3);
        Event event = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", true, address, user, list1);
        eventRepository.save(event);
        Invitation invitation = TestsRepositoryUtil.createInvitation("pending", "ana are mere", event, user);
        invitationService.saveInvitation(invitation);
    }

    @Test
    @Transactional
    public void assertThatLoggedUserIsRecipientUser() {

        List<Invitation> list = invitationRepository.findAll();
        boolean isTheSameUser = validationAccessService.verifyUserAccessForInvitationEntity(list.get(0).getId());
        assertThat(isTheSameUser).isEqualTo(false);

    }

    @Test
    @Transactional
    public void assertThatLoggedUserIsRecipientUserWithInvalidInvitationId() {

        boolean isTheSameUser = validationAccessService.verifyUserAccessForInvitationEntity(100L);
        assertThat(isTheSameUser).isEqualTo(false);

    }

    @Test
    @Transactional
    public void assertThatLoggedUserIsRecipientUserWithNullInvitationId() {

        List<Invitation> list = invitationRepository.findAll();
        boolean isTheSameUser = validationAccessService.verifyUserAccessForInvitationEntity(null);
        assertThat(isTheSameUser).isEqualTo(false);

    }


}
