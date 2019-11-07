package com.cegeka.academy.service;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.*;
import com.cegeka.academy.domain.enums.ChallengeStatus;
import com.cegeka.academy.domain.enums.InvitationStatus;
import com.cegeka.academy.domain.enums.UserChallengeStatus;
import com.cegeka.academy.repository.*;
import com.cegeka.academy.service.dto.ChallengeDTO;
import com.cegeka.academy.service.dto.UserChallengeDTO;
import com.cegeka.academy.service.mapper.UserChallengeMapper;
import com.cegeka.academy.service.userChallenge.UserChallengeService;
import com.cegeka.academy.web.rest.errors.InvalidInvitationStatusException;
import com.cegeka.academy.web.rest.errors.InvalidUserChallengeStatusException;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import com.cegeka.academy.web.rest.errors.WrongOwnerException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AcademyProjectApp.class)
@Transactional
public class UserChallengeServiceTest {

    @Autowired
    private UserChallengeRepository userChallengeRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeCategoryRepository challengeCategoryRepository;

    @Autowired
    private UserChallengeService userChallengeService;

    @Autowired
    private ChallengeAnswerRepository challengeAnswerRepository;

    private Invitation invitation;
    private User user;
    private Challenge challenge, challenge2;
    private ChallengeCategory challengeCategory;
    private UserChallengeDTO userChallengeDTO;
    private UserChallenge userChallenge;
    private ChallengeAnswer challengeAnswer;
    private User usedUser;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


    @BeforeEach
    public void init() {

        user = new User();
        user.setLogin("ana");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail("ana@gmail.com");
        user.setFirstName("ana");
        user.setLastName("doe");
        user.setImageUrl("http://placehold.it/50x50");
        user.setLangKey("mmmm");
        usedUser = userRepository.save(user);

        invitation = new Invitation();
        invitation.setDescription("invitationDescription");
        invitation.setStatus(InvitationStatus.PENDING.toString());
        invitation.setUser(usedUser);
        invitation.setEvent(null);
        invitationRepository.save(invitation);
        Date startDate = null;
        try {
            startDate = sdf.parse("22/09/2020");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = new Date();

        challengeCategory = new ChallengeCategory();
        challengeCategory.setName("challengeCategory");
        challengeCategory.setDescription("challengeCategoryDescription");
        challengeCategoryRepository.save(challengeCategory);

        challenge = new Challenge();
        challenge.setCreator(usedUser);
        challenge.setPoints(5.22);
        challenge.setStartDate(startDate);
        challenge.setEndDate(endDate);
        challenge.setStatus(ChallengeStatus.PRIVATE.toString());
        challenge.setDescription("description");
        challenge.setChallengeCategory(challengeCategoryRepository.findAll().get(0));
        challengeRepository.save(challenge);

        challenge2 = new Challenge();
        challenge2.setCreator(usedUser);
        challenge2.setPoints(5.22);
        challenge2.setStartDate(startDate);
        challenge2.setEndDate(endDate);
        challenge2.setStatus("new");
        challenge2.setDescription("description");
        challenge2.setChallengeCategory(challengeCategoryRepository.findAll().get(0));
        challengeRepository.save(challenge2);

        userChallenge = new UserChallenge();
        userChallenge.setUser(usedUser);
        userChallenge.setInvitation(invitationRepository.findAll().get(0));
        userChallenge.setChallenge(challengeRepository.findAll().get(0));
        userChallenge.setStatus("status");
        userChallenge.setPoints(2.22);
        userChallenge.setStartTime(new Date());
        userChallenge.setEndTime(new Date());
        userChallengeRepository.save(userChallenge);

        challengeAnswer = new ChallengeAnswer();
        challengeAnswer.setVideoAt("videoAt");
        challengeAnswer.setAnswer("answer");

        challengeAnswerRepository.save(challengeAnswer);

        userChallengeDTO = UserChallengeMapper.convertUserChallengeToUserChallengeDTO(userChallenge);

    }

    @AfterEach
    public void destroy() {

        invitationRepository.deleteAll();
        userChallengeRepository.deleteAll();
        challengeRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    public void testGetChallengesByUserIdIsWorking() throws NotFoundException {

        List<UserChallengeDTO> results = userChallengeService.getUserChallengesByUserId(usedUser.getId());
        assertThat(results.get(0).getChallenge().getId()).isEqualTo(userChallenge.getChallenge().getId());
        assertThat(results.get(0).getStartTime()).isEqualTo(userChallenge.getStartTime());
        assertThat(results.get(0).getEndTime()).isEqualTo(userChallenge.getEndTime());
        assertThat(results.get(0).getPoints()).isEqualTo(userChallenge.getPoints());
        assertThat(results.get(0).getStatus()).isEqualTo(userChallenge.getStatus());
        assertThat(results.get(0).getChallengeAnswer()).isEqualTo(userChallenge.getChallengeAnswer());
        assertThat(results.get(0).getInvitation().getId()).isEqualTo(userChallenge.getInvitation().getId());
        assertThat(results.get(0).getUser().getId()).isEqualTo(userChallenge.getUser().getId());

    }

    @Test
    public void testGetChallengesByUserIdWithNoResult() {

        Assertions.assertThrows(NotFoundException.class, () -> {

            userChallengeService.getUserChallengesByUserId(200L);

        });

    }

    @Test
    public void testUpdateUserChallengeStatusIsWorking() throws NotFoundException, InvalidUserChallengeStatusException {

        UserChallenge existingUserChallenge = userChallengeRepository.findAll().get(0);

        userChallengeService.updateUserChallengeStatus(existingUserChallenge.getId(), UserChallengeStatus.ACCEPTED.toString());

        assertThat(userChallengeRepository.findAll().get(0).getId()).isEqualTo(existingUserChallenge.getId());
        assertThat(userChallengeRepository.findAll().get(0).getStatus()).isEqualTo(UserChallengeStatus.ACCEPTED.toString());

    }

    @Test
    public void testUpdateUserChallengeStatusWithInvalidId() {

        Assertions.assertThrows(NotFoundException.class, () -> {

            userChallengeService.updateUserChallengeStatus(100L, UserChallengeStatus.ACCEPTED.toString());

        });

    }

    @Test
    public void testUpdateUserChallengeStatusWithInvalidStatus() {

        Assertions.assertThrows(InvalidUserChallengeStatusException.class, () -> {

            userChallengeService.updateUserChallengeStatus(userChallengeRepository.findAll().get(0).getId(), "status");

        });

    }

    @Test
    public void testUpdateUserChallengeInvitationStatusIsWorking() throws NotFoundException, InvalidInvitationStatusException {

        UserChallenge existingUserChallenge = userChallengeRepository.findAll().get(0);

        userChallengeService.updateUserChallengeInvitationStatus(existingUserChallenge.getId(), InvitationStatus.CANCELED.toString());

        assertThat(userChallengeRepository.findAll().get(0).getId()).isEqualTo(existingUserChallenge.getId());
        assertThat(userChallengeRepository.findAll().get(0).getInvitation().getStatus()).isEqualTo(InvitationStatus.CANCELED.toString());

    }

    @Test
    public void testUpdateUserChallengeInvitationStatusWithInvalidId() {

        Assertions.assertThrows(NotFoundException.class, () -> {

            userChallengeService.updateUserChallengeInvitationStatus(100L, InvitationStatus.CANCELED.toString());

        });

    }

    @Test
    public void testUpdateUserChallengeInvitationStatusWithInvalidStatus() {

        Assertions.assertThrows(InvalidInvitationStatusException.class, () -> {

            userChallengeService.updateUserChallengeInvitationStatus(userChallengeRepository.findAll().get(0).getId(), "status");
        });

    }

    @Test
    public void testRateUserByOwner() throws WrongOwnerException {
        userChallengeDTO.setPoints(49);
        double actual = userChallengeDTO.getPoints();
        double expected = userChallengeService.rateUser(userChallengeDTO, usedUser.getId()).getPoints();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testRateUserByWrongOwner() {
        userChallengeDTO.setPoints(49);

        Assertions.assertThrows(WrongOwnerException.class, () -> {
            userChallengeService.rateUser(userChallengeDTO, 4L);
        });

    }

    @Test
    public void testRateUserWhenInvitationIdIsWrong() {
        userChallengeDTO.setPoints(49);
        userChallengeDTO.getInvitation().setId(200L);

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            userChallengeService.rateUser(userChallengeDTO, challenge.getCreator().getId());
        });
    }

    @Test
    public void testRateUserWhenUserIdIsWrong() {
        userChallengeDTO.setPoints(49);
        userChallengeDTO.getUser().setId(7L);

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            userChallengeService.rateUser(userChallengeDTO, challenge.getCreator().getId());
        });

    }

    @Test
    public void testRateUserWhenChallengeIdIsWrong() {
        userChallengeDTO.setPoints(49);
        userChallengeDTO.getChallenge().setId(5L);

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            userChallengeService.rateUser(userChallengeDTO, challenge.getCreator().getId());
        });

    }

    @Test
    public void testUpdateAnswerForExistingUserChallengeIsWorking() throws NotFoundException {

        userChallengeService.addUserChallengeAnswer(userChallenge, challengeAnswer);

        assertThat(userChallengeRepository.findAllByChallengeAnswerId(challengeAnswer.getId()).size()).isEqualTo(1);
    }

    @Test
    public void testInitUserChallengeIsWorking() throws NotFoundException {

        userChallengeService.initUserChallenge(challenge2, invitation);

        Optional<UserChallenge> userChallengeOptional = userChallengeRepository
                .findByUserIdAndChallengeIdAndInvitationId(
                        invitation.getUser().getId(),
                        challenge2.getId(),
                        invitation.getId());
        assertThat(userChallengeOptional.isPresent()).isEqualTo(true);
    }

    @Test
    public void testInitUserChallengeThrowsNotFoundExceptionForMissingChallenge() throws NotFoundException {

        Challenge temporaryChallenge = new Challenge();
        temporaryChallenge.setId(200L);

        Assertions.assertThrows(NotFoundException.class,
                () -> userChallengeService.initUserChallenge(temporaryChallenge, invitation));

    }

    @Test
    public void testInitUserChallengeThrowsNotFoundExceptionForMissingInvitation() {
        Invitation temporaryInvitation = new Invitation();
        temporaryInvitation.setId(200L);

        Assertions.assertThrows(NotFoundException.class,
                () -> userChallengeService.initUserChallenge(challenge2, temporaryInvitation));
    }

    @Test
    public void testInitUserChallengeThrowsNotFoundExceptionForMissingUser() {
        User temporaryUser = new User();
        temporaryUser.setId(200L);

        Invitation temporaryInvitation = new Invitation();
        temporaryInvitation.setId(5L);
        temporaryInvitation.setUser(temporaryUser);

        Assertions.assertThrows(NotFoundException.class,
                () -> userChallengeService.initUserChallenge(challenge2, temporaryInvitation));
    }

    @Test
    public void testAddUserChallengeAnswerThrowsNotFoundExceptionForMissingUserChallenge() {

        UserChallenge temporaryUserChallenge = new UserChallenge();
        temporaryUserChallenge.setId(1000L);

        Assertions.assertThrows(NotFoundException.class,
                () -> userChallengeService.addUserChallengeAnswer(temporaryUserChallenge, challengeAnswer));
    }

    @Test
    public void testAddUserChallengeAnswerThrowsNotFoundExceptionForMissingAnswer() {

        ChallengeAnswer temporaryUserAnswer = new ChallengeAnswer();
        temporaryUserAnswer.setId(1000L);

        Assertions.assertThrows(NotFoundException.class,
                () -> userChallengeService.addUserChallengeAnswer(userChallenge, temporaryUserAnswer));
    }

    @Test
    public void testGetNextChallengesIsWorkingWithPrivateChallenge() throws NotFoundException {


        List<ChallengeDTO> challengeDTOList = userChallengeService.getNextChallengesForAnUser(usedUser.getId());

        assertThat(challengeDTOList.size()).isEqualTo(1);
        assertThat(challengeDTOList.get(0).getId()).isEqualTo(userChallengeRepository.findAllByUserId(usedUser.getId()).get(0).getChallenge().getId());

    }

    @Test
    public void testGetNextChallengesIsWorkingWithPublicChallenge() throws NotFoundException {

        userChallenge.getChallenge().setStatus(ChallengeStatus.PUBLIC.toString());
        userChallenge.setInvitation(null);
        userChallengeRepository.save(userChallenge);

        List<ChallengeDTO> challengeDTOList = userChallengeService.getNextChallengesForAnUser(usedUser.getId());

        assertThat(challengeDTOList.size()).isEqualTo(1);
        assertThat(challengeDTOList.get(0).getId()).isEqualTo(userChallengeRepository.findAllByUserId(usedUser.getId()).get(0).getChallenge().getId());

    }

    @Test
    public void testGetNextChallengesWithNoUserChallengesForAnUser() {

        Assertions.assertThrows(NotFoundException.class, () -> {
            userChallengeService.getNextChallengesForAnUser(444L);
        });
    }

    @Test
    public void testGetNextChallengesWithNoChallengesWithValidInvitation() {

        userChallenge.getInvitation().setStatus(InvitationStatus.CANCELED.toString());
        userChallengeRepository.save(userChallenge);

        Assertions.assertThrows(NotFoundException.class, () -> {
            userChallengeService.getNextChallengesForAnUser(usedUser.getId());
        });
    }

    @Test
    public void testGetNextChallengesWithNoChallengeWithValidDate() {

        Date startDate = null;
        try {
            startDate = sdf.parse("12/03/2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        userChallenge.getChallenge().setStartDate(startDate);
        userChallengeRepository.save(userChallenge);

        Assertions.assertThrows(NotFoundException.class, () -> {
            userChallengeService.getNextChallengesForAnUser(usedUser.getId());
        });
    }

    @Test
    public void getChallengesForAnUserWithPendingInvitation() throws NotFoundException {

        List<ChallengeDTO> result = userChallengeService.getChallengesByInvitationStatus(usedUser.getId(), InvitationStatus.PENDING);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(userChallenge.getChallenge().getId());

    }

    @Test
    public void getChallengesForAnUserWithNoPendingInvitation() {

        userChallenge.getInvitation().setStatus(InvitationStatus.ACCEPTED.toString());
        userChallengeRepository.save(userChallenge);

        Assertions.assertThrows(NotFoundException.class, () -> {

            userChallengeService.getChallengesByInvitationStatus(usedUser.getId(), InvitationStatus.PENDING);

        });

    }

    @Test
    public void getChallengesForAnUserWithPendingInvitationWithNoResult() {

        Assertions.assertThrows(NotFoundException.class, () -> {

            userChallengeService.getChallengesByInvitationStatus(200L, InvitationStatus.PENDING);

        });

    }
}
