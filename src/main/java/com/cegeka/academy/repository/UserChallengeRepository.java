package com.cegeka.academy.repository;

import com.cegeka.academy.domain.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {

    Optional<UserChallenge> findByUserIdAndChallengeIdAndInvitationId(Long userId, Long challengeId, Long invitationId);

    Optional<UserChallenge> findByUserIdAndChallengeId(Long userId, Long challengeId);

    Optional<UserChallenge> findByChallengeAnswerId(Long challengeAnswerId);

    List<UserChallenge> findAllByUserId(Long userId);

    List<UserChallenge> findAllByChallengeAnswerId(Long answerId);

    List<UserChallenge> findAllByChallengeId(Long challengeId);

    List<UserChallenge> findAllByUserIdAndInvitationStatus(Long userId, String status);
}
