package com.cegeka.academy.repository;

import com.cegeka.academy.domain.ChallengeAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeAnswerRepository extends JpaRepository<ChallengeAnswer, Long> {

}
