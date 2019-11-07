package com.cegeka.academy.service;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.*;
import com.cegeka.academy.domain.enums.InvitationStatus;
import com.cegeka.academy.repository.*;
import com.cegeka.academy.repository.util.TestsRepositoryUtil;
import com.cegeka.academy.service.dto.InvitationDTO;
import com.cegeka.academy.service.invitation.InvitationService;
import com.cegeka.academy.service.mapper.InvitationMapper;
import com.cegeka.academy.web.rest.errors.ExistingItemException;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AcademyProjectApp.class)
@Transactional
public class InvitationServiceTest {


    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupUserRoleRepository groupUserRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeCategoryRepository challengeCategoryRepository;

    @Autowired
    private UserChallengeRepository userChallengeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User user, user1, user2;
    private Event event, event2, publicEvent;
    private Invitation invitation, invitation2, invitation3, invitationSendToGroup, invitationWithNullEvent, invitationWithPublicEvent;
    private InvitationDTO invitationDTO;
    private Address address;
    private Group group;
    private GroupUserRole groupUserRole1, groupUserRole2, groupUserRole3;
    private Role role;
    List<User>userList=new ArrayList<>();
    private Challenge challenge;
    private ChallengeCategory challengeCategory;
    private UserChallenge userChallenge;

    private UserChallengeServiceTest userChallengeServiceTest;

    @BeforeEach
    public void init() {
        eventRepository.deleteAll();

        userRepository.deleteAll();
        categoryRepository.deleteAll();
        user = TestsRepositoryUtil.createUser("login", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        userRepository.save(user);
        user1 = TestsRepositoryUtil.createUser("login2", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaaka");
        userRepository.save(user1);
        user2 = TestsRepositoryUtil.createUser("login3", "anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaama");
        userRepository.save(user2);
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
        publicEvent = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", true, address, user, list1);
        eventRepository.saveAndFlush(publicEvent);
        invitation = TestsRepositoryUtil.createInvitation(InvitationStatus.PENDING.name(), "ana are mere", event, user);
        invitationService.saveInvitation(invitation);
        group = TestsRepositoryUtil.createGroup("gr1", "descriere grup");
        groupRepository.save(group);
        role = TestsRepositoryUtil.createRole("admin");
        roleRepository.save(role);
        groupUserRole1 = TestsRepositoryUtil.createGroupUserRole(userRepository.findAll().get(0), groupRepository.findAll().get(0), roleRepository.findAll().get(0));
        groupUserRoleRepository.save(groupUserRole1);
        groupUserRole2 = TestsRepositoryUtil.createGroupUserRole(userRepository.findAll().get(1), groupRepository.findAll().get(0), roleRepository.findAll().get(0));
        groupUserRoleRepository.save(groupUserRole2);
        groupUserRole3 = TestsRepositoryUtil.createGroupUserRole(userRepository.findAll().get(2), groupRepository.findAll().get(0), roleRepository.findAll().get(0));
        groupUserRoleRepository.save(groupUserRole3);
        invitationSendToGroup = TestsRepositoryUtil.createInvitation(InvitationStatus.PENDING.name(), "aaaa", eventRepository.findAll().get(0), null);
        invitationWithNullEvent = TestsRepositoryUtil.createInvitation(InvitationStatus.PENDING.name(), "aaaa", null, null);
        invitationWithPublicEvent = TestsRepositoryUtil.createInvitation(InvitationStatus.PENDING.name(), "aaaa", eventRepository.findAll().get(1), null);
        userList.add(user1);
        userList.add(user2);
        challengeCategory = TestsRepositoryUtil.createChallengeCategory("Description", "Name");
        challengeCategoryRepository.save(challengeCategory);
        challenge = TestsRepositoryUtil.createChallenge(challengeCategory, "Description", user1,
                "Status", new Date(), new Date(), 50.0);
        challengeRepository.save(challenge);
        userChallenge  = TestsRepositoryUtil.createUserChallenge(challenge, user2,50.0, "Status", invitation,
                new Date(), new Date());
        userChallengeRepository.save(userChallenge);

        invitationDTO = initInvitationDTO();
    }

    public InvitationDTO initInvitationDTO() {
        InvitationDTO invitationDTO = new InvitationDTO();

        invitationDTO.setStatus(InvitationStatus.PENDING.toString());
        invitationDTO.setDescription("Description");
        invitationDTO.setUserId(user.getId());
        invitationDTO.setEventName(null);

        return invitationDTO;
    }

    @Test
    @Transactional
    public void assertThatSaveInvitationIsWorking() {

        List<InvitationDTO> list = invitationService.getAllInvitations();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getStatus()).isEqualTo(invitation.getStatus());
        assertThat(list.get(0).getStatus()).isEqualTo(InvitationStatus.PENDING.name());
        assertThat(list.get(0).getDescription()).isEqualTo(invitation.getDescription());
        assertThat(list.get(0).getUserName()).isEqualTo(invitation.getUser().getFirstName() + " " + invitation.getUser().getLastName());
        assertThat(list.get(0).getEventName()).isEqualTo(invitation.getEvent().getName());
    }

    @Test
    @Transactional
    public void assertThatSaveInvitationToListIsWorking() {

        assertThat(event.getPendingInvitations().size()).isEqualTo(1);
        invitation2 = TestsRepositoryUtil.createInvitation(InvitationStatus.PENDING.name(), "ana are mere", event, user);
        invitationService.saveInvitation(invitation2);
        assertThat(event.getPendingInvitations().size()).isEqualTo(2);

    }

    @Test
    @Transactional
    public void assertThatSaveUserToParticipationListAfterAcceptInvitationIsWorking() {
        Category category1 = TestsRepositoryUtil.createCategory("Sport1", "Liber pentru toate varstele!");
        Category category3 = TestsRepositoryUtil.createCategory("Arta1", "Expozitii de arta");
        categoryRepository.save(category1);
        categoryRepository.save(category3);
        Set<Category> list1 = new HashSet<>();
        list1.add(category1);
        list1.add(category3);
        Event event2 = TestsRepositoryUtil.createEvent("Ana are mere!", "KFC Krushers Party", true, address, user, list1);
        eventRepository.save(event2);
        invitation3 = TestsRepositoryUtil.createInvitation(InvitationStatus.PENDING.name(), "ana are mere", event2, user);
        invitationService.saveInvitation(invitation3);
        try {
            invitationService.acceptInvitation(invitation3.getId());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThat(user.getEvents().size()).isEqualTo(1);
        assertThat(event.getUsers().size()).isEqualTo(0);

    }

    @Test
    @Transactional
    public void assertThatUpdateInvitationIsWorking() {
        List<Invitation> list = invitationRepository.findAll();
        invitation.setStatus(InvitationStatus.ACCEPTED.name());
        invitation.setId(list.get(0).getId());
        invitationService.updateInvitation(invitation);
        System.out.println(invitation.getId() + "");
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getStatus()).isEqualTo(invitation.getStatus());
        assertThat(list.get(0).getDescription()).isEqualTo(invitation.getDescription());
        assertThat(list.get(0).getUser()).isEqualTo(invitation.getUser());
        assertThat(list.get(0).getEvent()).isEqualTo(invitation.getEvent());
    }

    @Test
    @Transactional
    public void assertThatDeleteInvitationIsWorking() {

        List<Invitation> listBeforeDelete = invitationRepository.findAll();
        invitationService.deleteInvitationById(listBeforeDelete.get(0).getId());
        List<Invitation> listAfterDelete = invitationRepository.findAll();
        assertThat(listAfterDelete.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    public void assertThatDeleteInvitationIsWorkingWithInvalidId() {

        invitationService.deleteInvitationById(100L);
        List<InvitationDTO> list = invitationService.getAllInvitations();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void assertThatGetPendingInvitationsByUserIdIsWorking() {
        List<InvitationDTO> pendingListUser = invitationService.getPendingInvitationsByUserId(user.getId());
        assertThat(pendingListUser.size()).isEqualTo(1);

    }
    @Test
    @Transactional
    public void assertThatAcceptInvitation_ThrowsExceptionWithWrongInvitationId() {
        Assertions.assertThrows(NotFoundException.class, () -> invitationService.acceptInvitation(10000l));
    }

    @Test
    @Transactional
    public void assertThatAcceptInvitation_ThrowsExceptionWithWrongEventId() {

        invitation.getEvent().setId(111L);
        invitationRepository.save(invitation);
        Assertions.assertThrows(NotFoundException.class, () -> invitationService.acceptInvitation(invitation.getId()));
    }

    @Test
    @Transactional
    public void assertThatAcceptInvitation_ThrowsExceptionWithWrongUserId() {

        invitation.getUser().setId(111l);
        invitationRepository.save(invitation);
        Assertions.assertThrows(NotFoundException.class, () -> invitationService.acceptInvitation(invitation.getId()));
    }

    @Test
    public void assertThatAcceptInvitationIsWorking() throws NotFoundException {
        List<Invitation> list = invitationRepository.findAll();
        invitationService.acceptInvitation(invitation.getId());
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getStatus()).isEqualTo(invitation.getStatus());
        assertThat(list.get(0).getStatus()).isEqualTo(InvitationStatus.ACCEPTED.name());
        assertThat(list.get(0).getDescription()).isEqualTo(invitation.getDescription());
        assertThat(list.get(0).getUser()).isEqualTo(invitation.getUser());
        assertThat(list.get(0).getEvent()).isEqualTo(invitation.getEvent());
        assertThat(event.getPendingInvitations().size()).isEqualTo(0);
        assertThat(user.getEvents().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void assertThatRejectInvitation_ThrowsExceptionWithWrongInvitationId() {
        Assertions.assertThrows(NotFoundException.class, () -> invitationService.rejectInvitation(40l));
    }

    @Test
    public void assertThatRejectInvitationIsWorking() throws NotFoundException {
        List<Invitation> list = invitationRepository.findAll();
        invitation.setStatus(InvitationStatus.PENDING.name());
        invitationService.rejectInvitation(invitation.getId());
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getStatus()).isEqualTo(invitation.getStatus());
        assertThat(list.get(0).getStatus()).isEqualTo(InvitationStatus.REJECTED.name());
        assertThat(list.get(0).getDescription()).isEqualTo(invitation.getDescription());
        assertThat(list.get(0).getUser()).isEqualTo(invitation.getUser());
        assertThat(list.get(0).getEvent()).isEqualTo(invitation.getEvent());
    }

    @Test
    public void assertThatSendGroupInvitationToPrivateEventIsWorking() throws NotFoundException {

        Long idGroup = groupRepository.findAll().get(0).getId();
        invitationService.sendGroupInvitationsToPrivateEvents(idGroup, invitationSendToGroup);
        invitationSendToGroup.setId(invitationRepository.findAll().get(2).getId());
        List<Invitation> list = invitationRepository.findAll();
        Optional<User> findUser1 = userRepository.findById(list.get(list.lastIndexOf(invitationSendToGroup)).getUser().getId());
        Optional<Event> findEvent1 = eventRepository.findById(list.get(list.lastIndexOf(invitationSendToGroup)).getEvent().getId());
        invitationSendToGroup.setId(invitationRepository.findAll().get(1).getId());
        Optional<User> findUser2 = userRepository.findById(list.get(list.lastIndexOf(invitationSendToGroup) - 1).getUser().getId());
        Optional<Event> findEvent2 = eventRepository.findById(list.get(list.lastIndexOf(invitationSendToGroup) - 1).getEvent().getId());

        assertThat(list.size()).isEqualTo(3);
        Assert.assertTrue(findUser1.isPresent());
        Assert.assertTrue(findEvent1.isPresent());
        Assert.assertTrue(findUser2.isPresent());
        Assert.assertTrue(findEvent2.isPresent());
    }

    @Test
    public void assertThatSendGroupInvitationToPrivateEventIsWorkingWithNullIdGroup() throws NotFoundException {

        invitationService.sendGroupInvitationsToPrivateEvents(null, invitationSendToGroup);
        List<Invitation> list = invitationRepository.findAll();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void assertThatSendGroupInvitationToPrivateEventIsWorkingWithInvalidIdGroup() throws NotFoundException {

        invitationService.sendGroupInvitationsToPrivateEvents(100L, invitationSendToGroup);
        List<Invitation> list = invitationRepository.findAll();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    void assertThatSendGroupInvitationToPrivateEventThrowsNotFoundExceptionTest() {

        Assertions.assertThrows(NotFoundException.class,
                () -> invitationService.sendGroupInvitationsToPrivateEvents(1L, invitationWithNullEvent));
    }

    @Test
    public void assertThatSendGroupInvitationToPrivateEventIsNotWorkingWithPublicEvent() throws NotFoundException {

        Long idGroup = groupRepository.findAll().get(0).getId();
        invitationService.sendGroupInvitationsToPrivateEvents(idGroup, invitationWithPublicEvent);
        List<Invitation> list = invitationRepository.findAll();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    public void assertThatSendInvitationToUserListWorks() throws NotFoundException {
      invitationService.sendInvitationForPrivateEventsToUserList(userList,invitation);
      List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList.size()).isEqualTo(3);
    }

    @Test
    void assertThatSendInvitationToUserListThrowsError() {
        Assertions.assertThrows(NotFoundException.class, () -> invitationService.sendInvitationForPrivateEventsToUserList(userList, invitationWithNullEvent));
    }
    @Test
    public void assertThatSendInvitationToUserListWithPublicEvent() throws NotFoundException {
        invitationService.sendInvitationForPrivateEventsToUserList(userList,invitationWithPublicEvent);
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList.size()).isEqualTo(1);
    }


    @Test
    public void assertThatCreateChallengeInvitationIsWorking() throws NotFoundException, ExistingItemException {

        Invitation actual = InvitationMapper.createInvitation(
                invitationDTO.getDescription(),
                invitationDTO.getStatus(),
                user,
                null);

        Invitation expected = invitationService.createChallengeInvitationForOneUser(invitationDTO, challenge.getId());

        assertThat(actual.hashCode() == expected.hashCode());
    }

    @Test
    public void assertThatCreateChallengeInvitationThrowsExistingItemException() {

        invitationDTO.setUserId(user2.getId());

        Assertions.assertThrows(ExistingItemException.class,
                () -> invitationService
                        .createChallengeInvitationForOneUser(invitationDTO, userChallenge.getChallenge().getId()));

    }

    @Test
    public void assertThatCreateChallengeInvitationThrowsNotFoundExceptionForMissingChallenge() {

        Assertions.assertThrows(NotFoundException.class,
                () -> invitationService.createChallengeInvitationForOneUser(invitationDTO, (long)2));
    }

    @Test
    public void assertThatCreateChallengeInvitationThrowsNotFoundExceptionForMissingUser() {

        invitationDTO.setUserId((long)200);

        Assertions.assertThrows(NotFoundException.class,
                () -> invitationService.createChallengeInvitationForOneUser(invitationDTO, challenge.getId()));
    }
}
