package com.cegeka.academy.repository;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.domain.ChallengeCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AcademyProjectApp.class)
@Transactional
class ChallengeCategoryTest {

    @Autowired
    private ChallengeCategoryRepository challengeCategoryRepository;

    private ChallengeCategory challengeCategory;

    @BeforeEach
    public void init(){
        challengeCategory =  new ChallengeCategory();
        challengeCategory.setName("category1");
        challengeCategory.setDescription("description1");
        challengeCategoryRepository.save(challengeCategory);
    }

    @Test
    public void testAddChallengeCategory(){
        assertThat(challengeCategoryRepository.findAll().size()).isEqualTo(1);
        assertThat(challengeCategoryRepository.findAll().get(0).getName()).isEqualTo("category1");
        assertThat(challengeCategoryRepository.findAll().get(0).getDescription()).isEqualTo("description1");
    }

    @Test
    public void testFindByName(){
        assertThat(challengeCategoryRepository.findByName("category1")).isEqualTo(challengeCategoryRepository.findAll().get(0));
    }

    @Test
    public void testFindByNameWithNoResult(){
        assertThat(challengeCategoryRepository.findByName("category2")).isEqualTo(null);
    }
}