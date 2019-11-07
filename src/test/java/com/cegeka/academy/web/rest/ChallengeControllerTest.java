package com.cegeka.academy.web.rest;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.Challenge;
import com.cegeka.academy.domain.ChallengeCategory;
import com.cegeka.academy.domain.User;
import com.cegeka.academy.repository.ChallengeCategoryRepository;
import com.cegeka.academy.repository.ChallengeRepository;
import com.cegeka.academy.repository.EventRepository;
import com.cegeka.academy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AcademyProjectApp.class)
public class ChallengeControllerTest {

    private MockMvc restMockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ChallengeController challengeController;

    @Autowired
    private ChallengeCategoryRepository challengeCategoryRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;


    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        challengeRepository.deleteAll();
        challengeCategoryRepository.deleteAll();
        userRepository.deleteAll();

        restMockMvc = MockMvcBuilders.standaloneSetup(challengeController).setControllerAdvice(globalExceptionHandler).build();

        User user = new User();

        user.setEmail("gigi.gogaie@gmail.com");
        user.setFirstName("Gogaie");
        user.setLastName("Momaie");
        user.setPassword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        user.setLogin("gigel@purcel.com");

        userRepository.save(user);


        ChallengeCategory challengeCategory = new ChallengeCategory();
        challengeCategory.setDescription("Categoria speciala");
        challengeCategory.setName("BiliRuLoc");

        challengeCategoryRepository.saveAndFlush(challengeCategory);

        Challenge challenge = new Challenge();

        challenge.setCreator(user);
        challenge.setStartDate(new Date(System.currentTimeMillis()));
        challenge.setEndDate(new Date(System.currentTimeMillis()));
        challenge.setStatus("Activa");
        challenge.setDescription("Nimic");
        challenge.setPoints(10);
        challenge.setChallengeCategory(challengeCategory);

        challengeRepository.save(challenge);
    }

    @Test
    void testDeleteChallengeException() throws Exception {
        restMockMvc.perform(delete("/challenge/0")).andExpect(status().isNotFound());
    }

    @Test
    void testDeleteChallenge() throws Exception {
        long id = challengeRepository.findAll().get(0).getId();
        restMockMvc.perform(delete("/challenge/"+id)).andExpect(status().isOk());
    }

    @Test
    void testGetUserChallengesByUserIdException() throws Exception {
        restMockMvc.perform(get("/challenge/user/0")).andExpect(status().isNotFound());
    }

    @Test
    void getChallengeByIdException() throws Exception {
        restMockMvc.perform(get("challenge/0")).andExpect(status().isNotFound());
    }
}
