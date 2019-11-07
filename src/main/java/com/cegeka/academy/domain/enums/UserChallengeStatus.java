package com.cegeka.academy.domain.enums;

import com.cegeka.academy.web.rest.errors.InvalidUserChallengeStatusException;

import java.util.Arrays;

public enum UserChallengeStatus {

    ACCEPTED, CANCELED;

    public static UserChallengeStatus getUserChallengeStatus(String status) throws InvalidUserChallengeStatusException {

        return Arrays.stream(values())
                .filter(userChallengeStatus -> status.equalsIgnoreCase(userChallengeStatus.toString()))
                .findFirst()
                .orElseThrow(() -> new InvalidUserChallengeStatusException().setMessage("Status is invalid"));
    }
}
