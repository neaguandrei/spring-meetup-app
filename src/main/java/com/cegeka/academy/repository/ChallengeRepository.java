package com.cegeka.academy.repository;

import com.cegeka.academy.domain.Challenge;
import com.cegeka.academy.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    List<Challenge> findAllByStatus(String status);

    List<Challenge> findAllByCreator(User creator);

    List<Challenge> findAllByStartDate(Date startTime);

    List<Challenge> findAllByEndDate(Date endTime);

    List<Challenge> findAllByPoints(Double points);

    List<Challenge> findAllByCreatorId(Long creatorId);
}
