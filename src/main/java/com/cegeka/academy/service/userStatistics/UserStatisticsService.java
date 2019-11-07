package com.cegeka.academy.service.userStatistics;

import com.cegeka.academy.domain.UserStatistics;

public interface UserStatisticsService {

    UserStatistics getUserStatistics(int age, String gender);

}
