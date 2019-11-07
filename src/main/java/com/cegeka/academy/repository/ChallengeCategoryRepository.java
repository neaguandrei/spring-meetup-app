package com.cegeka.academy.repository;

import com.cegeka.academy.domain.ChallengeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeCategoryRepository extends JpaRepository<ChallengeCategory,Long> {
    ChallengeCategory findByName(String name);
}
