package com.cegeka.academy.service.challenge;

import com.cegeka.academy.domain.Challenge;
import com.cegeka.academy.domain.UserChallenge;
import com.cegeka.academy.domain.enums.InvitationStatus;
import com.cegeka.academy.domain.enums.SortingParam;
import com.cegeka.academy.repository.ChallengeCategoryRepository;
import com.cegeka.academy.domain.enums.ChallengeStatus;
import com.cegeka.academy.repository.ChallengeRepository;
import com.cegeka.academy.repository.UserChallengeRepository;
import com.cegeka.academy.service.dto.ChallengeCategoryDTO;
import com.cegeka.academy.service.dto.UserChallengeDTO;
import com.cegeka.academy.service.dto.UserDTO;
import com.cegeka.academy.service.mapper.UserChallengeMapper;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import liquibase.util.CollectionUtil;
import net.bytebuddy.description.type.TypeDefinition;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.cegeka.academy.web.rest.errors.InvalidFieldException;
import com.cegeka.academy.service.dto.ChallengeDTO;
import com.cegeka.academy.service.mapper.ChallengeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChallengeServiceImp implements ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeCategoryRepository challengeCategoryRepository;

    @Autowired
    private UserChallengeRepository userChallengeRepository;

    @Override
    public void deleteChallenge(long id) throws NotFoundException {
            if(challengeRepository.findById(id).isPresent()) {
                challengeRepository.deleteById(id);
            }
            else {
                throw new NotFoundException().setMessage("Nu exista challenge cu id-ul: " + id);
            }
    }

    private Logger logger =  LoggerFactory.getLogger(ChallengeServiceImp.class);

    @Override
    public void saveChallenge(ChallengeDTO challengeDTO) {

        Challenge challenge = ChallengeMapper.convertChallengeDTOToChallenge(challengeDTO);

        challengeRepository.save(challenge);

        logger.info("Challenge has been saved");
        
    }

    @Override
    public ChallengeDTO updateChallenge(Long challengeId, ChallengeDTO challengeDTO) throws NotFoundException {


        validateChallengeDTOForUpdate(challengeId, challengeDTO);

        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(
                () -> new NotFoundException().setMessage("Challenge-ul " + challengeId + " nu exista")
        );

        challenge = ChallengeMapper.enrichChallenge(challengeDTO, challenge);

        challengeRepository.save(challenge);

        return challengeDTO;

    }

    @Override
    public Set<ChallengeDTO> getChallengesByUserId(long id) throws NotFoundException {

        List<UserChallenge> userChallengesList = userChallengeRepository.findAllByUserId(id);

        LinkedHashSet userChallengesSet = userChallengesList.stream()
                .map(userChallenge -> getChallengeDTOFromUserChallenge(userChallenge))
                .collect(Collectors.toCollection(LinkedHashSet::new));


        if(userChallengesSet.size() == 0)
        {
            throw new NotFoundException().setMessage("User-ul cu id-ul " + id + "nu a fost asociat cu nicio provocare");
        }

        return userChallengesSet;
    }

    @Override
    public List<ChallengeDTO> getChallengesByCreatorId(Long creatorId) throws NotFoundException {

        List<Challenge> challenges = challengeRepository.findAllByCreatorId(creatorId);

        if(challenges == null || challenges.isEmpty()){

            throw new NotFoundException().setMessage("List is empty.");
        }

       return challenges.stream().map(challenge -> ChallengeMapper.convertChallengeToChallengeDTO(challenge)).collect(Collectors.toList());

    }

    @Override
    public List<ChallengeDTO> getPublicChallenges() throws NotFoundException {

        List<Challenge> publicChallenges = challengeRepository.findAllByStatus(ChallengeStatus.PUBLIC.toString());

        if(publicChallenges == null || publicChallenges.isEmpty()) {

            throw new NotFoundException().setMessage("List is empty");

        }

        List<Challenge> validChallengeList = publicChallenges.stream().filter(challenge -> isValidChallenge(challenge)).collect(Collectors.toList());

        if(validChallengeList == null || validChallengeList.isEmpty()){

            throw new NotFoundException().setMessage("List is empty");

        }

        return validChallengeList.stream().map(challenge -> ChallengeMapper.convertChallengeToChallengeDTO(challenge)).collect(Collectors.toList());

    }


    @Override
    public ChallengeDTO getChallengeById(long id) throws NotFoundException {

        Optional<Challenge> challengeOptional = challengeRepository.findById(id);

        challengeOptional.orElseThrow(
                () -> new NotFoundException().setMessage("Provocarea cu id-ul: " + id + " nu exista")
        );

        Challenge challenge = challengeOptional.get();

        return ChallengeMapper.convertChallengeToChallengeDTO(challenge);
    }

    @Override
    public List<UserChallengeDTO> getChallengeRanking(Long challengeId, String sortingParam) throws NotFoundException {
        List<UserChallenge> userChallengeList = userChallengeRepository.findAllByChallengeId(challengeId);

        if(CollectionUtils.isEmpty(userChallengeList))
        {
            throw new NotFoundException().setMessage("Nu exista participanti la challenge-ul: " + challengeId);
        }

        SortingParam eSortingParam = SortingParam.getSortingParam(sortingParam);
        eSortingParam.sort(userChallengeList);

        return userChallengeList.stream()
                .map(UserChallengeMapper::convertUserChallengeToUserChallengeDTO).collect(Collectors.toList());
    }

    private ChallengeDTO getChallengeDTOFromUserChallenge(UserChallenge userChallenge)
    {
        Challenge challenge = userChallenge.getChallenge();
        return ChallengeMapper.convertChallengeToChallengeDTO(challenge);
    }

    private boolean isValidChallenge(Challenge challenge){

        return DateUtils.isSameDay(challenge.getEndDate(), new Date()) || challenge.getEndDate().after(new Date());

    }


    private boolean  doesChallengeCategoryExist(long challengeCategoryId)
    {
        return challengeCategoryRepository.findById(challengeCategoryId).isPresent();
    }


    private void validateChallengeDTOForUpdate(long pathChallengeId, ChallengeDTO challengeDTO)
    {

        if(pathChallengeId != challengeDTO.getId()) {

            throw new InvalidFieldException().setMessage("Id-ul challenge-ului din path trebuie ca corespunda cu cel din DTO");

        }

        ChallengeCategoryDTO challengeCategoryDTO = challengeDTO.getChallengeCategory();

        if(challengeCategoryDTO != null)
        {
            if(challengeCategoryDTO.getId() == null) {

                challengeDTO.setChallengeCategory(null);

            }
            else
                if(!doesChallengeCategoryExist(challengeCategoryDTO.getId())){

                    throw new InvalidFieldException().setMessage("Categoria aleasa nu exista in baza de date");

                }
        }
    }

}
