package com.cegeka.academy.service;

import com.cegeka.academy.AcademyProjectApp;
import com.cegeka.academy.service.serviceValidation.ExpirationCheckService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AcademyProjectApp.class)
public class ExpirationCheckServiceTest {

    @Autowired
    private ExpirationCheckService expirationCheckService;

    @Test
    public void assertThatEndDateIsAfterToday() {
        Date endDate = new Date();
        endDate.setMonth(10);
        boolean isAvailable = expirationCheckService.availabilityCheck(endDate);
        assertThat(isAvailable).isEqualTo(true);
    }

    @Test
    public void assertThatEndDateIsBeforeToday() {
        Date endDate = new Date();
        endDate.setMonth(1);
        boolean isAvailable = expirationCheckService.availabilityCheck(endDate);
        assertThat(isAvailable).isEqualTo(false);
    }

    @Test
    public void assertThatEndDateIsAvailableWithNullInput() {

        boolean isAvailable = expirationCheckService.availabilityCheck(null);
        assertThat(isAvailable).isEqualTo(false);
    }

}
