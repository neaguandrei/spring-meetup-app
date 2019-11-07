package com.cegeka.academy.service.challengeAnswer;

import com.cegeka.academy.service.dto.ChallengeAnswerDTO;
import com.cegeka.academy.web.rest.errors.ExistingItemException;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ChallengeAnswerService {

    void saveChallengeAnswer(Long userChallengeId, ChallengeAnswerDTO challengeAnswerDTO) throws NotFoundException, ExistingItemException;

    void updateChallengeAnswer(Long id, ChallengeAnswerDTO challengeAnswerDTO) throws NotFoundException;

    void deleteChallengeAnswer(Long userId, Long challengeId) throws NotFoundException;

    void uploadAnswerPhoto(Long challengeAnswerId, MultipartFile image) throws IOException, NotFoundException;
}
