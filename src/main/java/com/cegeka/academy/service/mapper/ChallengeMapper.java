package com.cegeka.academy.service.mapper;

import com.cegeka.academy.domain.Challenge;
import com.cegeka.academy.domain.ChallengeCategory;
import com.cegeka.academy.service.dto.ChallengeCategoryDTO;
import com.cegeka.academy.service.dto.ChallengeDTO;


public class ChallengeMapper {


    public static Challenge convertChallengeDTOToChallenge(ChallengeDTO challengeDTO){

        if(challengeDTO == null){

            return null;

        }

        Challenge challenge = new Challenge();
        challenge.setEndDate(challengeDTO.getEndDate());
        challenge.setStartDate(challengeDTO.getStartDate());
        challenge.setDescription(challengeDTO.getDescription());
        challenge.setPoints(challengeDTO.getPoints());
        challenge.setStatus(challengeDTO.getStatus());
        challenge.setEndDate(challengeDTO.getEndDate());

        if (challengeDTO.getCreator() != null) {

            challenge.setCreator(new UserMapper().userDTOToUser(challengeDTO.getCreator()));
            challenge.getCreator().setId(challengeDTO.getCreator().getId());
        }

        if(challengeDTO.getChallengeCategory() != null) {

            challenge.setChallengeCategory(convertChallengeCategoryDTOToChallengeCategory(challengeDTO.getChallengeCategory()));
            challenge.getChallengeCategory().setId(challengeDTO.getChallengeCategory().getId());

        }

        return challenge;
    }

    public static ChallengeDTO convertChallengeToChallengeDTO(Challenge challenge)
    {
        if(challenge == null) {

            return null;

        } else {

            ChallengeDTO challengeDTO = new ChallengeDTO();

            challengeDTO.setId(challenge.getId());
            challengeDTO.setChallengeCategory(convertChallengeCategoryToChallengeCategoryDTO(challenge.getChallengeCategory()));
            challengeDTO.setCreator(new UserMapper().userToUserDTO(challenge.getCreator()));
            challengeDTO.setDescription(challenge.getDescription());
            challengeDTO.setStartDate(challenge.getStartDate());
            challengeDTO.setEndDate(challenge.getEndDate());
            challengeDTO.setPoints(challenge.getPoints());
            challengeDTO.setStatus(challenge.getStatus());

            return challengeDTO;
        }

    }

    public static ChallengeCategory convertChallengeCategoryDTOToChallengeCategory(ChallengeCategoryDTO challengeCategoryDTO){

        if(challengeCategoryDTO == null){

            return null;

        } else {

            ChallengeCategory challengeCategory = new ChallengeCategory();
            challengeCategory.setName(challengeCategoryDTO.getName());
            challengeCategory.setDescription(challengeCategoryDTO.getDescription());

            return challengeCategory;
        }

    }

    public static ChallengeCategoryDTO convertChallengeCategoryToChallengeCategoryDTO(ChallengeCategory challengeCategory){

        if(challengeCategory == null){

            return null;

        } else {

            ChallengeCategoryDTO challengeCategoryDTO = new ChallengeCategoryDTO();
            challengeCategoryDTO.setId(challengeCategory.getId());
            challengeCategoryDTO.setName(challengeCategory.getName());
            challengeCategoryDTO.setDescription(challengeCategory.getDescription());

            return challengeCategoryDTO;
        }

    }

    public static Challenge enrichChallenge(ChallengeDTO challengeDTO, Challenge challenge) {

        challenge.setStartDate(challengeDTO.getStartDate());
        challenge.setEndDate(challengeDTO.getEndDate());
        challenge.setPoints(challengeDTO.getPoints());
        challenge.setDescription(challengeDTO.getDescription());
        challenge.setStatus(challengeDTO.getStatus());
        challenge.setChallengeCategory(enrichChallengeCategory(challenge.getChallengeCategory() , challengeDTO.getChallengeCategory()));

        return challenge;
    }

    public static ChallengeCategory enrichChallengeCategory(ChallengeCategory challengeCategory, ChallengeCategoryDTO challengeCategoryDTO) {

        if(!challengeCategory.getId().equals(challengeCategoryDTO.getId())) {

            challengeCategory = new ChallengeCategory();
            challengeCategory.setId(challengeCategoryDTO.getId());
        }
        challengeCategory.setDescription(challengeCategoryDTO.getDescription());
        challengeCategory.setName(challengeCategoryDTO.getName());

        return challengeCategory;
    }

}
