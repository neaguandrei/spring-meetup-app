package com.cegeka.academy.service.challengeAnswer;

import com.cegeka.academy.domain.ChallengeAnswer;
import com.cegeka.academy.domain.UserChallenge;
import com.cegeka.academy.repository.ChallengeAnswerRepository;
import com.cegeka.academy.repository.UserChallengeRepository;
import com.cegeka.academy.service.dto.ChallengeAnswerDTO;
import com.cegeka.academy.service.mapper.ChallengeAnswerMapper;
import com.cegeka.academy.web.rest.errors.ExistingItemException;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
public class ChallengeAnswerServiceImp implements ChallengeAnswerService {

    @Autowired
    private ChallengeAnswerRepository challengeAnswerRepository;
    @Autowired
    private UserChallengeRepository userChallengeRepository;

    private Logger logger = LoggerFactory.getLogger(ChallengeAnswerServiceImp.class);

    @Override
    public void saveChallengeAnswer(Long userChallengeId, ChallengeAnswerDTO challengeAnswerDTO) throws NotFoundException, ExistingItemException {

        UserChallenge userChallenge = userChallengeRepository.findById(userChallengeId)
                .orElseThrow(()-> new NotFoundException().setMessage("User challenge does not exists"));

        if(userChallenge.getChallengeAnswer() != null){

            throw new ExistingItemException().setMessage("Challenge answer already exists");
        }

        ChallengeAnswer saveChallengeAnswer = ChallengeAnswerMapper.convertChallengeAnswerDTOToChallengeAnswer(challengeAnswerDTO);

        ChallengeAnswer result = challengeAnswerRepository.save(saveChallengeAnswer);

        userChallenge.setChallengeAnswer(result);

        userChallengeRepository.save(userChallenge);

        logger.info("Challenge answer with id: " + result.getId() + " has been saved.");
    }

    @Override
    public void updateChallengeAnswer(Long id, ChallengeAnswerDTO challengeAnswerDTO) throws NotFoundException {

        ChallengeAnswer challengeAnswer = challengeAnswerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException().setMessage("Challenge answer not exists."));

        challengeAnswer.setAnswer(challengeAnswerDTO.getAnswer());
        challengeAnswer.setVideoAt(challengeAnswerDTO.getVideoAt());

        ChallengeAnswer result = challengeAnswerRepository.save(challengeAnswer);

        logger.info("Challenge answer with id: " + result.getId() + " was updated.");

    }

    @Override
    public void deleteChallengeAnswer(Long userId, Long challengeId) throws NotFoundException {

        UserChallenge userChallenge = getUserChallengeByUserIdAndChallengeId(userId, challengeId);

        if (userChallenge.getChallengeAnswer() == null) {
            throw new NotFoundException().setMessage("Answer doesn't exist");
        }

        challengeAnswerRepository.findById(userChallenge.getChallengeAnswer().getId())
                .orElseThrow(() -> new NotFoundException().setMessage("Answer not found"));

        ChallengeAnswer deleteChallengeAnswer = userChallenge.getChallengeAnswer();

        userChallenge.setChallengeAnswer(null);
        userChallengeRepository.save(userChallenge);

        challengeAnswerRepository.delete(deleteChallengeAnswer);

        logger.info("Challenge answer was deleted.");
    }

    @Override
    public void uploadAnswerPhoto(Long challengeAnswerId, MultipartFile image) throws NotFoundException, IOException {

        UserChallenge userChallenge = userChallengeRepository.findByChallengeAnswerId(challengeAnswerId).orElseThrow(
                () -> new NotFoundException().setMessage("User challenge not found"));

        ChallengeAnswer challengeAnswer = userChallenge.getChallengeAnswer();
        challengeAnswer.setImage(image.getBytes());
        challengeAnswerRepository.save(challengeAnswer);
    }

    public UserChallenge getUserChallengeByUserIdAndChallengeId(Long userId, Long challengeId) throws NotFoundException {
        UserChallenge userChallenge = userChallengeRepository.findByUserIdAndChallengeId(userId, challengeId)
                .orElseThrow(() -> new NotFoundException().setMessage("User challenge not found"));

        return userChallenge;
    }
}
