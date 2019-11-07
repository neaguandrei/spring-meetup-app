package com.cegeka.academy.repository;


import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.ChallengeAnswer;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AcademyProjectApp.class)
@Transactional
public class ChallengeAnswerRepositoryTest {

    @Autowired
    private ChallengeAnswerRepository challengeAnswerRepository;

    private ChallengeAnswer challengeAnswer;

    public MultipartFile initImage() throws IOException {
        File image = new File("src/test/resources/images/poza123.jpg");
        FileInputStream input = new FileInputStream(image);

        MultipartFile imageFile = new MockMultipartFile("image", IOUtils.toByteArray(input));

        return imageFile;
    }

    @BeforeEach
    public void setUp() throws IOException {

        challengeAnswer = new ChallengeAnswer();
        challengeAnswer.setImage(initImage().getBytes());
        challengeAnswer.setVideoAt("videoAt");
        challengeAnswer.setAnswer("answer");

    }

    @AfterEach
    public void destroy(){

        challengeAnswerRepository.deleteAll();
    }

    @Test
    public void testSaveChallengeAnswer(){

        assertThat(challengeAnswerRepository.save(challengeAnswer)).isEqualTo(challengeAnswerRepository.findAll().get(0));

    }
}
