package com.cegeka.academy.service.mapper;

import com.cegeka.academy.domain.Challenge;
import com.cegeka.academy.domain.ChallengeAnswer;
import com.cegeka.academy.domain.Invitation;
import com.cegeka.academy.domain.UserChallenge;
import com.cegeka.academy.service.dto.*;

public class UserChallengeMapper {


    public static UserChallenge convertUserChallengeDTOToUserChallenge(UserChallengeDTO userChallengeDTO){

        if(userChallengeDTO != null){

            UserChallenge userChallenge = new UserChallenge();
            userChallenge.setStatus(userChallengeDTO.getStatus());
            userChallenge.setPoints(userChallengeDTO.getPoints());
            userChallenge.setStartTime(userChallengeDTO.getStartTime());
            userChallenge.setEndTime(userChallengeDTO.getEndTime());

            if(userChallengeDTO.getUser() != null) {

                userChallenge.setUser(new UserMapper().userDTOToUser(userChallengeDTO.getUser()));
                userChallenge.getUser().setId(userChallengeDTO.getUser().getId());
            }

            if(userChallengeDTO.getInvitation() != null) {

                userChallenge.setInvitation(convertInvitationChallengeDTOTOInvitation(userChallengeDTO.getInvitation()));
                userChallenge.getInvitation().setId(userChallengeDTO.getInvitation().getId());

            }

            if(userChallengeDTO.getChallenge() != null) {

                userChallenge.setChallenge(convertChallengeDTOToChallenge(userChallengeDTO.getChallenge()));
                userChallenge.getChallenge().setId(userChallengeDTO.getChallenge().getId());

            }

            if(userChallengeDTO.getChallengeAnswer() != null){

                userChallenge.setChallengeAnswer(convertChallengeAnswerDTOToChallengeAnswer(userChallengeDTO.getChallengeAnswer()));
                userChallenge.getChallengeAnswer().setId(userChallengeDTO.getChallengeAnswer().getId());

            }

            return userChallenge;
        }

        return null;
    }

    public static UserChallengeDTO convertUserChallengeToUserChallengeDTO(UserChallenge userChallenge){

        if(userChallenge != null){

            UserChallengeDTO userChallengeDTO = new UserChallengeDTO();
            userChallengeDTO.setId(userChallenge.getId());
            userChallengeDTO.setUser(new UserMapper().userToUserDTO(userChallenge.getUser()));
            userChallengeDTO.setInvitation(convertInvitationToInvitationChallengeDTO(userChallenge.getInvitation()));
            userChallengeDTO.setChallenge(convertChallengeToChallengeDTO(userChallenge.getChallenge()));
            userChallengeDTO.setStatus(userChallenge.getStatus());
            userChallengeDTO.setPoints(userChallenge.getPoints());
            userChallengeDTO.setStartTime(userChallenge.getStartTime());
            userChallengeDTO.setEndTime(userChallenge.getEndTime());
            userChallengeDTO.setChallengeAnswer(convertChallengeAnswerToChallengeAnswerDTO(userChallenge.getChallengeAnswer()));

            return userChallengeDTO;
        }

        return null;
    }

    public static ChallengeDTO convertChallengeToChallengeDTO(Challenge challenge){

        if(challenge == null){

            return null;

        }

        ChallengeDTO challengeDTO = new ChallengeDTO();
        challengeDTO.setId(challenge.getId());
        challengeDTO.setCreator(new UserMapper().userToUserDTO(challenge.getCreator()));
        challengeDTO.setStartDate(challenge.getStartDate());
        challengeDTO.setEndDate(challenge.getEndDate());
        challengeDTO.setPoints(challenge.getPoints());
        challengeDTO.setStatus(challenge.getStatus());
        challengeDTO.setDescription(challenge.getDescription());
        challengeDTO.setChallengeCategory(ChallengeMapper.convertChallengeCategoryToChallengeCategoryDTO(challenge.getChallengeCategory()));

        return challengeDTO;
    }

    public static Challenge convertChallengeDTOToChallenge(ChallengeDTO challengeDTO){

        if(challengeDTO == null){

            return null;

        }

        Challenge challenge = new Challenge();

        if(challengeDTO.getCreator() != null) {

             challenge.setCreator(new UserMapper().userDTOToUser(challengeDTO.getCreator()));
             challenge.getCreator().setId(challengeDTO.getCreator().getId());
        }

        challenge.setStartDate(challengeDTO.getStartDate());
        challenge.setEndDate(challengeDTO.getEndDate());
        challenge.setPoints(challengeDTO.getPoints());
        challenge.setDescription(challengeDTO.getDescription());
        challenge.setStatus(challengeDTO.getStatus());

        if(challengeDTO.getChallengeCategory() != null) {

            challenge.setChallengeCategory(ChallengeMapper.convertChallengeCategoryDTOToChallengeCategory(challengeDTO.getChallengeCategory()));
            challenge.getChallengeCategory().setId(challengeDTO.getChallengeCategory().getId());

        }

        return challenge;
    }

    public static InvitationChallengeDTO convertInvitationToInvitationChallengeDTO(Invitation invitation){

        if(invitation == null){

            return null;
        }

        InvitationChallengeDTO invitationChallengeDTO = new InvitationChallengeDTO();
        invitationChallengeDTO.setUser(new UserMapper().userToUserDTO(invitation.getUser()));
        invitationChallengeDTO.setId(invitation.getId());
        invitationChallengeDTO.setStatus(invitation.getStatus());

        return invitationChallengeDTO;
    }

    public static Invitation convertInvitationChallengeDTOTOInvitation(InvitationChallengeDTO invitationChallengeDTO){

        if(invitationChallengeDTO == null){

            return null;
        }

        Invitation invitation = new Invitation();
        invitation.setUser(new UserMapper().userDTOToUser(invitationChallengeDTO.getUser()));
        invitation.setStatus(invitationChallengeDTO.getStatus());

        return invitation;
    }

    public static ChallengeAnswer convertChallengeAnswerDTOToChallengeAnswer(ChallengeAnswerDTO challengeAnswerDTO){

        if(challengeAnswerDTO == null){

            return null;

        } else {

            ChallengeAnswer challengeAnswer = new ChallengeAnswer();
            challengeAnswer.setImage(challengeAnswerDTO.getImage());
            challengeAnswer.setVideoAt(challengeAnswerDTO.getVideoAt());
            challengeAnswer.setAnswer(challengeAnswerDTO.getAnswer());

            return challengeAnswer;

        }
    }

    public static ChallengeAnswerDTO convertChallengeAnswerToChallengeAnswerDTO(ChallengeAnswer challengeAnswer){

        if(challengeAnswer == null){

            return null;

        } else {

            ChallengeAnswerDTO challengeAnswerDTO = new ChallengeAnswerDTO();
            challengeAnswerDTO.setId(challengeAnswer.getId());
            challengeAnswerDTO.setImage(challengeAnswer.getImage());
            challengeAnswerDTO.setVideoAt(challengeAnswer.getVideoAt());
            challengeAnswerDTO.setAnswer(challengeAnswer.getAnswer());

            return challengeAnswerDTO;

        }
    }

}
