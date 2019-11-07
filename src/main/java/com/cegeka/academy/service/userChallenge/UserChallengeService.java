package com.cegeka.academy.service.userChallenge;

import com.cegeka.academy.domain.Challenge;
import com.cegeka.academy.domain.ChallengeAnswer;
import com.cegeka.academy.domain.Invitation;
import com.cegeka.academy.domain.UserChallenge;
import com.cegeka.academy.domain.enums.InvitationStatus;
import com.cegeka.academy.service.dto.ChallengeDTO;
import com.cegeka.academy.service.dto.UserChallengeDTO;
import com.cegeka.academy.web.rest.errors.InvalidInvitationStatusException;
import com.cegeka.academy.web.rest.errors.InvalidUserChallengeStatusException;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import com.cegeka.academy.web.rest.errors.WrongOwnerException;

import java.util.List;
import java.util.NoSuchElementException;

public interface UserChallengeService {

    UserChallenge rateUser(UserChallengeDTO userChallengeDTO, Long ownerId)
            throws WrongOwnerException, NoSuchElementException;

    UserChallenge initUserChallenge(Challenge challenge, Invitation invitation) throws NotFoundException;

    List<ChallengeDTO> getNextChallengesForAnUser(Long userId) throws NotFoundException;

    List<ChallengeDTO> getChallengesByInvitationStatus(Long userId, InvitationStatus invitationStatus)
            throws NotFoundException;

    List<UserChallengeDTO> getUserChallengesByUserId(Long userId) throws NotFoundException;

    void addUserChallengeAnswer(UserChallenge userChallenge, ChallengeAnswer challengeAnswer) throws NotFoundException;

    void updateUserChallengeStatus(Long userChallengeId, String status)
            throws NotFoundException, InvalidUserChallengeStatusException;

    void updateUserChallengeInvitationStatus(Long userChallengeId, String status)
            throws NotFoundException, InvalidInvitationStatusException;
}
