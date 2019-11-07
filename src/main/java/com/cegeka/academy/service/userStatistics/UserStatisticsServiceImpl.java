package com.cegeka.academy.service.userStatistics;

import com.cegeka.academy.domain.UserStatistics;
import com.cegeka.academy.repository.UserStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStatisticsServiceImpl implements UserStatisticsService {

    @Autowired
    UserStatisticsRepository userStatisticsRepository;

    @Override
    public UserStatistics getUserStatistics(int age, String gender) {
        return userStatisticsRepository.findOneByAgeAndGender(age,gender);
    }

}
