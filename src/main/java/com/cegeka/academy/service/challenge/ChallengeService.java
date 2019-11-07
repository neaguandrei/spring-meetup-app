package com.cegeka.academy.service.challenge;

import com.cegeka.academy.service.dto.ChallengeDTO;
import com.cegeka.academy.service.dto.UserChallengeDTO;
import com.cegeka.academy.web.rest.errors.NotFoundException;

import java.util.List;
import java.util.Set;

public interface ChallengeService {
    void deleteChallenge(long id) throws NotFoundException;

    void saveChallenge(ChallengeDTO challenge);

    ChallengeDTO updateChallenge(Long challengeId, ChallengeDTO challengeDTO) throws NotFoundException;

    Set<ChallengeDTO> getChallengesByUserId(long id) throws NotFoundException;

    ChallengeDTO getChallengeById(long id) throws NotFoundException;

    List<ChallengeDTO> getChallengesByCreatorId(Long creatorId) throws NotFoundException;

    List<ChallengeDTO> getPublicChallenges() throws NotFoundException;

    List<UserChallengeDTO> getChallengeRanking(Long challengeId, String sortingParam) throws NotFoundException;

}
