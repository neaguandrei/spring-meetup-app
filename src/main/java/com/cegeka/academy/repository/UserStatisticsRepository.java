package com.cegeka.academy.repository;


import com.cegeka.academy.domain.AgeIntervals;
import com.cegeka.academy.domain.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long> {

    @Query("select us from UserStatistics us where us.ageInterval=?1 and us.gender=?2")
    UserStatistics findOneByAgeAndGender(int age, String gender);

    
}
