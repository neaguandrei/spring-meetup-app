package com.cegeka.academy.service;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.*;
import com.cegeka.academy.domain.enums.ChallengeStatus;
import com.cegeka.academy.repository.*;
import com.cegeka.academy.service.challenge.ChallengeService;
import com.cegeka.academy.service.dto.ChallengeCategoryDTO;
import com.cegeka.academy.service.dto.ChallengeDTO;
import com.cegeka.academy.service.dto.UserChallengeDTO;
import com.cegeka.academy.service.dto.UserDTO;
import com.cegeka.academy.service.mapper.ChallengeMapper;
import com.cegeka.academy.service.mapper.UserChallengeMapper;
import com.cegeka.academy.service.mapper.UserMapper;
import com.cegeka.academy.web.rest.errors.InvalidFieldException;
import com.cegeka.academy.web.rest.errors.InvalidSortingParamException;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AcademyProjectApp.class)
@Transactional
public class ChallengeServiceTest {

    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChallengeCategoryRepository challengeCategoryRepository;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private UserChallengeRepository userChallengeRepository;

    private UserChallenge userChallenge;
    private UserChallenge userChallenge2;
    private UserChallenge userChallenge3;
    private Invitation invitation;
    private Invitation invitation2;
    private Challenge challenge;
    private Challenge challenge2;

    private ChallengeDTO challengeDTO;
    private UserDTO userDTO;
    private ChallengeCategoryDTO challengeCategoryDTO;

    long defaultChallengeId;

    @BeforeEach
    public void init(){

        User user;
        ChallengeCategory challengeCategory;

        user = new User();
        user.setLogin("login");
        user.setLastName("coximorinas");
        user.setPassword("anaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaanaana");
        Long idUser = userRepository.save(user).getId();

        userDTO = new UserDTO(user);
        userDTO.setId(idUser);

        challengeCategoryDTO = new ChallengeCategoryDTO();
        challengeCategoryDTO.setDescription("description");
        challengeCategoryDTO.setName("name");

        challengeCategory = ChallengeMapper.convertChallengeCategoryDTOToChallengeCategory(challengeCategoryDTO);

        Long challengeCategoryId = challengeCategoryRepository.save(challengeCategory).getId();

        challengeCategoryDTO.setId(challengeCategoryId);

        challengeDTO = new ChallengeDTO();
        challengeDTO.setCreator(userDTO);
        challengeDTO.setChallengeCategory(challengeCategoryDTO);
        challengeDTO.setDescription("description");
        challengeDTO.setEndDate(new Date());
        challengeDTO.setStartDate(new Date());
        challengeDTO.setPoints(22);
        challengeDTO.setStatus("status");

        challenge = new Challenge();

        challenge.setCreator(user);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            challenge.setStartDate(sdf.parse("12/09/2020"));
            challenge.setEndDate(sdf.parse("22/09/2020"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        challenge.setStatus(ChallengeStatus.PUBLIC.toString());
        challenge.setDescription("Nimic");
        challenge.setPoints(10);
        challenge.setChallengeCategory(challengeCategory);

        challenge2 = new Challenge();
        challenge2.setCreator(user);
        challenge2.setPoints(5.22);
        challenge2.setStartDate(new Date(System.currentTimeMillis()));
        challenge2.setEndDate(new Date(System.currentTimeMillis()));
        challenge2.setStatus("new");
        challenge2.setDescription("description");
        challenge2.setChallengeCategory(challengeCategory);

        invitation = new Invitation();
        invitation.setDescription("invitationDescription");
        invitation.setStatus("invitationStat");
        invitation.setUser(user);
        invitation.setEvent(null);

        invitation2 = new Invitation();
        invitation2.setDescription("invitationDescription");
        invitation2.setStatus("invitationStat");
        invitation2.setUser(user);
        invitation2.setEvent(null);


        userChallenge = new UserChallenge();
        userChallenge.setUser(user);
        userChallenge.setInvitation(invitation);
        userChallenge.setChallenge(challenge);
        userChallenge.setStatus("status");
        userChallenge.setPoints(10);
        userChallenge.setStartTime(new Date());
        userChallenge.setEndTime(new Date());


        userChallenge2 = new UserChallenge();
        userChallenge2.setUser(user);
        userChallenge2.setInvitation(invitation);
        userChallenge2.setChallenge(challenge2);
        userChallenge2.setStatus("status");
        userChallenge2.setPoints(2.22);
        userChallenge2.setStartTime(new Date());
        userChallenge2.setEndTime(new Date());

        userChallenge3 = new UserChallenge();
        userChallenge3.setUser(user);
        userChallenge3.setInvitation(invitation2);
        userChallenge3.setChallenge(challenge);
        userChallenge3.setStatus("status");
        userChallenge3.setPoints(2.2);
        userChallenge3.setStartTime(new Date());
        userChallenge3.setEndTime(new Date());
    }

    @Test
    public void testSaveChallenge(){

       challengeService.saveChallenge(challengeDTO);

       assertThat(challengeRepository.findAll().get(0).getCreator().getId()).isEqualTo(challengeDTO.getCreator().getId());
       assertThat(challengeRepository.findAll().get(0).getChallengeCategory().getId()).isEqualTo(challengeDTO.getChallengeCategory().getId());
       assertThat(challengeRepository.findAll().get(0).getDescription()).isEqualTo(challengeDTO.getDescription());
       assertThat(challengeRepository.findAll().get(0).getStartDate()).isEqualTo(challengeDTO.getStartDate());
       assertThat(challengeRepository.findAll().get(0).getEndDate()).isEqualTo(challengeDTO.getEndDate());
       assertThat(challengeRepository.findAll().get(0).getStatus()).isEqualTo(challengeDTO.getStatus());
       assertThat(challengeRepository.findAll().get(0).getPoints()).isEqualTo(challengeDTO.getPoints());

    }

    @Test
    void notFoundExceptionTest() {
        Assertions.assertThrows(NotFoundException.class,() -> {challengeService.deleteChallenge(0);});
    }

    @Test
    void deleteTest() throws NotFoundException {
        defaultChallengeId = challengeRepository.save(challenge).getId();
        challengeService.deleteChallenge(defaultChallengeId);
        Assertions.assertFalse(challengeRepository.findById(defaultChallengeId).isPresent());
    }

    @Test
    void emptyChallengeSetException() {

        Assertions.assertThrows(NotFoundException.class,()->{ challengeService.getChallengesByUserId(0); });
    }

    @Test
    void getChallengesByUserIdTest() throws NotFoundException {

        invitationRepository.save(invitation);
        challengeRepository.save(challenge);
        challengeRepository.save(challenge2);
        userChallengeRepository.save(userChallenge);
        userChallengeRepository.save(userChallenge2);

        Set<ChallengeDTO> challengeDTOSet = challengeService.getChallengesByUserId(userDTO.getId());

        Assertions.assertTrue(challengeDTOSet.contains(ChallengeMapper.convertChallengeToChallengeDTO(challenge)));
        Assertions.assertTrue(challengeDTOSet.contains(ChallengeMapper.convertChallengeToChallengeDTO(challenge2)));
    }

    @Test
    void getChallengeByIdWhenNoSuchElementException() {

        Assertions.assertThrows(NotFoundException.class, ()->{ challengeService.getChallengeById(0); });
    }

    @Test
    void getChallengeById() throws NotFoundException {

        Challenge expectedChallenge = challengeRepository.save(challenge);

        ChallengeDTO challengeDTO = challengeService.getChallengeById(expectedChallenge.getId());

        Assertions.assertEquals(expectedChallenge.getId(), challengeDTO.getId());
        Assertions.assertEquals(expectedChallenge.getCreator().getId(), challengeDTO.getCreator().getId());
        Assertions.assertEquals(expectedChallenge.getChallengeCategory().getId(), challengeDTO.getChallengeCategory().getId());
        Assertions.assertEquals(expectedChallenge.getDescription(), challengeDTO.getDescription());
        Assertions.assertEquals(expectedChallenge.getStartDate(), challengeDTO.getStartDate());
        Assertions.assertEquals(expectedChallenge.getEndDate(), challengeDTO.getEndDate());
        Assertions.assertEquals(expectedChallenge.getStatus(), challengeDTO.getStatus());
        Assertions.assertEquals(expectedChallenge.getPoints(), challengeDTO.getPoints());

    }

    @Test
    void updateChallenge() throws NotFoundException {
        challengeRepository.save(challenge);

        ChallengeDTO challengeDTO = ChallengeMapper.convertChallengeToChallengeDTO(challenge);

        long expectedId = challenge.getId();
        String expectedStatus = "updatedStatus";

        ChallengeCategory expectedChallengeCategory = new ChallengeCategory();
        expectedChallengeCategory.setName("updatedCategory");
        expectedChallengeCategory.setDescription("updatePerformed");

        challengeCategoryRepository.save(expectedChallengeCategory);

        ChallengeCategoryDTO expectedChallengeCategoryDTO;
        expectedChallengeCategoryDTO  = ChallengeMapper.convertChallengeCategoryToChallengeCategoryDTO(expectedChallengeCategory);

        challengeDTO.setStatus(expectedStatus);
        challengeDTO.setChallengeCategory(expectedChallengeCategoryDTO);

        ChallengeDTO actualChallengeDTO = challengeService.updateChallenge(challenge.getId(), challengeDTO);

        Challenge actualChallenge = ChallengeMapper.convertChallengeDTOToChallenge(actualChallengeDTO);

        String actualStatus = actualChallenge.getStatus();
        ChallengeCategory actualChallengeCategory = actualChallenge.getChallengeCategory();

        Assertions.assertEquals(expectedStatus, actualStatus);
        Assertions.assertEquals(expectedChallengeCategory, actualChallengeCategory);
    }

    @Test
    void updateChallengeNotFoundExceptionTest(){
        challengeDTO.setId(0L);
        Assertions.assertThrows(NotFoundException.class, ()->challengeService.updateChallenge(0L, challengeDTO));
    }


    @Test
    void invalidFieldExceptionTest_WITH_invalidChallengeCategoryId() {
        challengeRepository.save(challenge);

        ChallengeDTO challengeDTO = ChallengeMapper.convertChallengeToChallengeDTO(challenge);

        ChallengeCategory expectedChallengeCategory = new ChallengeCategory();
        expectedChallengeCategory.setId(0L);
        expectedChallengeCategory.setName("updatedCategory");
        expectedChallengeCategory.setDescription("updatePerformed");

        ChallengeCategoryDTO expectedChallengeCategoryDTO;
        expectedChallengeCategoryDTO  = ChallengeMapper.convertChallengeCategoryToChallengeCategoryDTO(expectedChallengeCategory);

        challengeDTO.setChallengeCategory(expectedChallengeCategoryDTO);

        Assertions.assertThrows(InvalidFieldException.class, () -> challengeService.updateChallenge(challenge.getId(), challengeDTO));
    }

    @Test
    void invalidFieldExceptionTest_WITH_differentCreatorId() {
        challengeRepository.save(challenge);

        ChallengeDTO challengeDTO = ChallengeMapper.convertChallengeToChallengeDTO(challenge);

        challengeDTO.getCreator().setId(0L);

        Assertions.assertThrows(InvalidFieldException.class, () -> challengeService.updateChallenge(0L, challengeDTO));
    }

    @Test
    void invalidFieldExceptionTest_WITH_pathCreatorIdDistinctFromDtoCreatorId() {
        challengeRepository.save(challenge);

        ChallengeDTO challengeDTO = ChallengeMapper.convertChallengeToChallengeDTO(challenge);

        Assertions.assertThrows(InvalidFieldException.class, () -> challengeService.updateChallenge(0L, challengeDTO));
    }

    @AfterEach
    public void destroy(){

        userChallengeRepository.deleteAll();
        challengeRepository.deleteAll();
        challengeCategoryRepository.deleteAll();
        invitationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testGetChallengesByCreatorId() throws NotFoundException {

        Challenge savedChallenge = challengeRepository.save(challenge);
        ChallengeDTO savedChallengeDTO = ChallengeMapper.convertChallengeToChallengeDTO(savedChallenge);

        List<ChallengeDTO> challengeDTOList = challengeService.getChallengesByCreatorId(savedChallengeDTO.getCreator().getId());

        assertThat(challengeDTOList.size()).isEqualTo(1);
        assertThat(challengeDTOList.get(0)).isEqualTo(savedChallengeDTO);

    }

    @Test
    public void testGetChallengesByCreatorIdEmptyList() {

        Assertions.assertThrows(NotFoundException.class, () -> {
            challengeService.getChallengesByCreatorId(100L);
        });

    }

    @Test
    public void testGetPublicChallenges() throws NotFoundException {

        Challenge savedChallenge = challengeRepository.save(challenge);
        ChallengeDTO savedChallengeDTO = ChallengeMapper.convertChallengeToChallengeDTO(savedChallenge);

        List<ChallengeDTO> challengeDTOList = challengeService.getPublicChallenges();

        assertThat(challengeDTOList.size()).isEqualTo(1);
        assertThat(challengeDTOList.get(0)).isEqualTo(savedChallengeDTO);

    }

    @Test
    public void testGetPublicChallengesEmptyList() {

        Assertions.assertThrows(NotFoundException.class, () -> {
            challengeService.getPublicChallenges();
        });

    }

    @Test
    void testGetChallengeRankOrderedByPoints() throws NotFoundException {
        invitationRepository.save(invitation);
        invitationRepository.save(invitation2);
        challengeRepository.save(challenge);
        userChallengeRepository.save(userChallenge);
        userChallengeRepository.save(userChallenge3);

        List<UserChallengeDTO> userChallengeDTOListExpected = new ArrayList<>();
        userChallengeDTOListExpected.add(UserChallengeMapper.convertUserChallengeToUserChallengeDTO(userChallenge));
        userChallengeDTOListExpected.add(UserChallengeMapper.convertUserChallengeToUserChallengeDTO(userChallenge3));

        List<UserChallengeDTO> userChallengeDTOListActual = challengeService.getChallengeRanking(challenge.getId(), "points");

        userChallengeDTOListExpected.sort((o1, o2) -> -Double.compare(o1.getPoints(), o2.getPoints()));

        Assertions.assertEquals(userChallengeDTOListExpected, userChallengeDTOListActual);
    }


    @Test
    void testGetChallengeRankOrderedByName() throws NotFoundException {
        invitationRepository.save(invitation);
        invitationRepository.save(invitation2);
        challengeRepository.save(challenge);
        userChallengeRepository.save(userChallenge);
        userChallengeRepository.save(userChallenge3);

        List<UserChallengeDTO> userChallengeDTOListExpected = new ArrayList<>();
        userChallengeDTOListExpected.add(UserChallengeMapper.convertUserChallengeToUserChallengeDTO(userChallenge));
        userChallengeDTOListExpected.add(UserChallengeMapper.convertUserChallengeToUserChallengeDTO(userChallenge3));

        List<UserChallengeDTO> userChallengeDTOListActual = challengeService.getChallengeRanking(challenge.getId(), "points");

        userChallengeDTOListExpected.sort((o1, o2) -> o1.getUser().getLastName().compareToIgnoreCase(o2.getUser().getLastName()));

        Assertions.assertEquals(userChallengeDTOListExpected, userChallengeDTOListActual);
    }

    @Test
    void testGetChallengeRankException(){
        challengeRepository.save(challenge);
        invitationRepository.save(invitation);
        userChallengeRepository.save(userChallenge);
        Assertions.assertThrows(NotFoundException.class, ()->challengeService.getChallengeRanking(0L, "points"));
        Assertions.assertThrows(InvalidSortingParamException.class,()->challengeService.getChallengeRanking(challenge.getId(),""));
    }

}
