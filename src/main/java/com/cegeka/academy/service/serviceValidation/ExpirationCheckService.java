package com.cegeka.academy.service.serviceValidation;

import com.cegeka.academy.service.invitation.InvitationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ExpirationCheckService {

    private Logger logger = LoggerFactory.getLogger(InvitationServiceImpl.class);

    public boolean availabilityCheck(Date endDate) {

        if (endDate != null) {

            Date today = new Date();

            if (endDate.before(today)) {

                logger.info("Event expired.");
                return false;
            }

            logger.info("The event is still available.");
            return true;

        } else {

            return false;
        }
    }
}
