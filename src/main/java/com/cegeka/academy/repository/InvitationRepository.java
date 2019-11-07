package com.cegeka.academy.repository;

import com.cegeka.academy.domain.Category;
import com.cegeka.academy.domain.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

     List<Invitation> findByStatus(String status);

     Set<Invitation> findAllByEvent_id(Long eventId);

     List<Invitation> findByUser_IdAndStatusIgnoreCase(Long userId, String status);
}
