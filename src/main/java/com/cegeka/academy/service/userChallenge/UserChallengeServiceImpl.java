package com.cegeka.academy.service.userChallenge;

import com.cegeka.academy.domain.*;
import com.cegeka.academy.domain.Challenge;
import com.cegeka.academy.domain.Invitation;
import com.cegeka.academy.domain.User;
import com.cegeka.academy.domain.UserChallenge;
import com.cegeka.academy.domain.enums.ChallengeStatus;
import com.cegeka.academy.domain.enums.InvitationStatus;
import com.cegeka.academy.domain.enums.UserChallengeStatus;
import com.cegeka.academy.repository.*;
import com.cegeka.academy.repository.ChallengeRepository;
import com.cegeka.academy.repository.InvitationRepository;
import com.cegeka.academy.repository.UserChallengeRepository;
import com.cegeka.academy.repository.UserRepository;
import com.cegeka.academy.service.dto.ChallengeDTO;
import com.cegeka.academy.service.dto.UserChallengeDTO;
import com.cegeka.academy.service.mapper.ChallengeMapper;
import com.cegeka.academy.service.mapper.UserChallengeMapper;
import com.cegeka.academy.web.rest.errors.InvalidInvitationStatusException;
import com.cegeka.academy.web.rest.errors.InvalidUserChallengeStatusException;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import com.cegeka.academy.web.rest.errors.WrongOwnerException;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserChallengeServiceImpl implements UserChallengeService {

    @Autowired
    private UserChallengeRepository userChallengeRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private ChallengeAnswerRepository challengeAnswerRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserChallengeDTO> getUserChallengesByUserId(Long userId) throws NotFoundException {

        List<UserChallenge> userChallengeList = userChallengeRepository.findAllByUserId(userId);

        if(Collections.isEmpty(userChallengeList)){

            throw new NotFoundException().setMessage("List is empty");

        }

        return userChallengeList.stream().map(userChallenge -> UserChallengeMapper.convertUserChallengeToUserChallengeDTO(userChallenge)).collect(Collectors.toList());

    }

    @Override
    public void updateUserChallengeStatus(Long userChallengeId, String status) throws NotFoundException, InvalidUserChallengeStatusException {

        UserChallenge userChallenge = userChallengeRepository.findById(userChallengeId)
                .orElseThrow(() -> new NotFoundException().setMessage("User challenge does not exists."));

        userChallenge.setStatus(UserChallengeStatus.getUserChallengeStatus(status).toString());

        userChallengeRepository.save(userChallenge);

    }

    @Override
    public void updateUserChallengeInvitationStatus(Long userChallengeId, String status) throws NotFoundException, InvalidInvitationStatusException {

        UserChallenge userChallenge = userChallengeRepository.findById(userChallengeId)
                .orElseThrow(() -> new NotFoundException().setMessage("User challenge does not exists."));

        if(userChallenge.getInvitation() == null){

            throw new NotFoundException().setMessage("Invitation does not exists");

        }

        userChallenge.getInvitation().setStatus(InvitationStatus.getInvitationStatus(status).toString());

        userChallengeRepository.save(userChallenge);

    }

    @Override
    public UserChallenge rateUser(UserChallengeDTO userChallengeDTO, Long ownerId)
            throws WrongOwnerException {

        long userId = userChallengeDTO.getUser().getId();
        long challengeId = userChallengeDTO.getChallenge().getId();
        long invitationId = userChallengeDTO.getInvitation().getId();
        UserChallenge userChallenge = userChallengeRepository
                .findByUserIdAndChallengeIdAndInvitationId(userId, challengeId, invitationId).orElseThrow(NoSuchElementException::new);

        userChallenge.setPoints(userChallengeDTO.getPoints());

        if (userChallenge.getChallenge().getCreator().getId().equals(ownerId)) {
            return userChallengeRepository.save(userChallenge);
        } else {
            throw new WrongOwnerException();
        }
    }

    @Override
    public UserChallenge initUserChallenge(Challenge challenge, Invitation invitation) throws NotFoundException {

        UserChallenge userChallenge = new UserChallenge();

        challengeRepository.findById(challenge.getId()).orElseThrow(
                () -> new NotFoundException().setMessage("Challenge not found"));
        invitationRepository.findById(invitation.getId()).orElseThrow(
                () -> new NotFoundException().setMessage("Invitation not found"));
        User invitedUser = invitation.getUser();
        userRepository.findById(invitedUser.getId()).orElseThrow(
                () -> new NotFoundException().setMessage("User not found"));

        userChallenge.setChallenge(challenge);
        userChallenge.setPoints(0);
        userChallenge.setUser(invitation.getUser());
        userChallenge.setInvitation(invitation);
        userChallenge.setStatus("Not started");
        userChallenge.setChallengeAnswer(null);
        userChallenge.setStartTime(new Date());
        userChallenge.setEndTime(new Date());

        return userChallengeRepository.save(userChallenge);
    }

    @Override
    public void addUserChallengeAnswer(UserChallenge userChallenge, ChallengeAnswer challengeAnswer) throws NotFoundException {

        userChallengeRepository.findById(userChallenge.getId()).orElseThrow(
                () -> new NotFoundException().setMessage("User challenge not found"));

        challengeAnswerRepository.findById(challengeAnswer.getId()).orElseThrow(
                () -> new NotFoundException().setMessage("Answer not found"));

        userChallenge.setChallengeAnswer(challengeAnswer);

        userChallengeRepository.save(userChallenge);
    }

    @Override
    public List<ChallengeDTO> getNextChallengesForAnUser(Long userId) throws NotFoundException {

        List<UserChallenge> userChallengeList = userChallengeRepository.findAllByUserId(userId);

        if(userChallengeList ==  null || userChallengeList.isEmpty()){

            throw new NotFoundException().setMessage("List is empty");

        }

        List<Challenge> futureChallenges = userChallengeList.stream()
                .filter(userChallenge -> hasUserChallengeValidChallenge(userChallenge) && isAfterToday(userChallenge.getChallenge().getStartDate()))
                .map(userChallenge -> userChallenge.getChallenge())
                .collect(Collectors.toList());

        if(futureChallenges ==  null || futureChallenges.isEmpty()){

            throw new NotFoundException().setMessage("List is empty");

        }

        return futureChallenges.stream()
                .map(challenge -> ChallengeMapper.convertChallengeToChallengeDTO(challenge))
                .collect(Collectors.toList());
    }

    @Override
    public List<ChallengeDTO> getChallengesByInvitationStatus(Long userId, InvitationStatus invitationStatus) throws NotFoundException {

        List<Challenge> challengeList = userChallengeRepository.findAllByUserIdAndInvitationStatus(userId, invitationStatus.toString())
                .stream()
                .map(userChallenge -> userChallenge.getChallenge())
                .collect(Collectors.toList());

        if(CollectionUtils.isEmpty(challengeList)){

            throw new NotFoundException().setMessage("List is empty");

        }

        return challengeList.stream()
                .map(ChallengeMapper::convertChallengeToChallengeDTO)
                .collect(Collectors.toList());

    }

    private boolean isAfterToday(Date date){

        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .isAfter(LocalDate.now());
    }

    private boolean isUserChallengeInvitationValid(UserChallenge userChallenge){

        return userChallenge.getInvitation() != null &&
                !userChallenge.getInvitation().getStatus().equalsIgnoreCase(InvitationStatus.CANCELED.toString()) &&
                !userChallenge.getInvitation().getStatus().equalsIgnoreCase(InvitationStatus.REJECTED.toString());
    }

    private boolean hasUserChallengeValidChallenge(UserChallenge userChallenge){

        return  userChallenge.getChallenge() != null &&
                (userChallenge.getChallenge().getStatus().equalsIgnoreCase(ChallengeStatus.PUBLIC.toString()) ||
                (userChallenge.getChallenge().getStatus().equalsIgnoreCase(ChallengeStatus.PRIVATE.toString()) &&
                        isUserChallengeInvitationValid(userChallenge)));

    }
}
