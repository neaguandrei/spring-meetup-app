package com.cegeka.academy.service.util;

import com.cegeka.academy.service.dto.EventDTO;
import com.cegeka.academy.service.dto.UserDTO;
import com.cegeka.academy.service.invitation.InvitationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class SortUtil {

    private static Logger logger = LoggerFactory.getLogger(InvitationServiceImpl.class);


    public static List<UserDTO> sortUsersByDate(List<UserDTO> users) {

        return users.stream().sorted((o1, o2) -> {

            if (o1.getCreatedDate() == null && o2.getCreatedDate() == null) {

                logger.info("Both data are null");
                return 0;

            } else if (o1.getCreatedDate() == null) {

                logger.info("First date is null");
                return 1;

            } else if (o2.getCreatedDate() == null) {

                logger.info("Second date is null");
                return -1;
            }

            return o2.getCreatedDate().compareTo(o1.getCreatedDate());

        }).collect(Collectors.toList());
    }

    public static List<UserDTO> sortUsersByName(List<UserDTO> users) {

        return users.stream().sorted((o1, o2) -> {

            if (o1.getFirstName() == null && o2.getFirstName() == null) {

                logger.info("Both first name are null");

                if (o1.getLastName() == null && o2.getLastName() == null) {

                    logger.info("Both last name are null");

                    return 0;

                } else if (o1.getLastName() == null) {

                    logger.info("First lastName is null");
                    return 1;

                } else if (o2.getLastName() == null) {

                    logger.info("Second lastName is null");
                    return -1;
                }

                return o2.getLastName().compareTo(o1.getLastName());


            } else if (o1.getFirstName() == null) {

                logger.info("First firstName is null");
                return 1;

            } else if (o2.getFirstName() == null) {

                logger.info("Second firstName is null");
                return -1;
            }

            return o2.getFirstName().compareTo(o1.getFirstName());

        }).collect(Collectors.toList());
    }


    public static List<EventDTO> sortEventsByStartDate(List<EventDTO> events) {

        return events.stream().sorted((o1, o2) -> {

            if (o1.getStartDate() == null && o2.getStartDate() == null) {

                logger.info("Both data are null");
                return 0;

            } else if (o1.getStartDate() == null) {

                logger.info("First date is null");
                return 1;

            } else if (o2.getStartDate() == null) {

                logger.info("Second date is null");
                return -1;
            }

            return o2.getStartDate().compareTo(o1.getStartDate());

        }).collect(Collectors.toList());
    }

}
