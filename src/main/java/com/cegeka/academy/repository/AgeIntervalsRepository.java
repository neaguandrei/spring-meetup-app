package com.cegeka.academy.repository;


import com.cegeka.academy.domain.Address;
import com.cegeka.academy.domain.AgeIntervals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgeIntervalsRepository extends JpaRepository<AgeIntervals, Long> {

    
}
