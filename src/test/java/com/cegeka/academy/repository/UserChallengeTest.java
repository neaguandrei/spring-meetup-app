package com.cegeka.academy.repository;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AcademyProjectApp.class)
@Transactional
public class UserChallengeTest {

    @Autowired
    private UserChallengeRepository userChallengeRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeAnswerRepository challengeAnswerRepository;

    private Invitation invitation = new Invitation();
    private User user = new User();
    private Challenge challenge = new Challenge();
    private ChallengeAnswer challengeAnswer = new ChallengeAnswer();

    @BeforeEach
    public void init() {

        invitation.setStatus("pending");
        invitationRepository.save(invitation);

        user.setLogin("LoginSetForTest");
        user.setPassword("42jIG0vHCTEWClhT5R2om2V5NpgOXNcQggP6YJOz2xMccBQzGDWgqUDLKOqZ");
        userRepository.save(user);

        challenge.setStartDate(new Date());
        challenge.setEndDate(new Date());
        challenge.setStatus("Active");
        challenge.setCreator(user);
        challengeRepository.save(challenge);

        challengeAnswer.setVideoAt("videoAt");
        challengeAnswer.setAnswer("answer");
        challengeAnswerRepository.save(challengeAnswer);
    }

    @AfterEach
    public void destroy(){

        if(user != null) {
            userRepository.delete(user);
        }

        if(invitation != null){
            invitationRepository.delete(invitation);
        }

        if(challenge != null){
            challengeRepository.delete(challenge);
        }

        if(challengeAnswer != null){
            challengeAnswerRepository.delete(challengeAnswer);
        }

    }

    public UserChallenge initObject() {
        UserChallenge userChallenge = new UserChallenge();
        userChallenge.setUser(user);
        userChallenge.setInvitation(invitation);
        userChallenge.setChallenge(challenge);
        userChallenge.setStatus("Active");
        userChallenge.setPoints(0);
        userChallenge.setStartTime(new Date());
        userChallenge.setEndTime(new Date());
        userChallenge.setChallengeAnswer(challengeAnswer);

        return userChallenge;
    }

    @Test
    public void testAddUserChallenge() {

        UserChallenge result = userChallengeRepository.save(initObject());

        assertThat(userChallengeRepository.findAll().size()).isEqualTo(1);
        assertThat(userChallengeRepository.findAll().get(0)).isEqualTo(result);
        assertThat(userChallengeRepository.findAll().get(0).getChallengeAnswer()).isEqualTo(challengeAnswerRepository.findAll().get(0));

    }

}
