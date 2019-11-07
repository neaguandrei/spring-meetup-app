package com.cegeka.academy.domain.enums;

import com.cegeka.academy.web.rest.errors.InvalidInvitationStatusException;

import java.util.Arrays;

public enum InvitationStatus {

    PENDING, ACCEPTED, REJECTED, CANCELED;

    public static InvitationStatus getInvitationStatus(String status) throws InvalidInvitationStatusException {

        return Arrays.stream(values())
                .filter(invitationStatus -> status.equalsIgnoreCase(invitationStatus.toString()))
                .findFirst()
                .orElseThrow(() -> new InvalidInvitationStatusException().setMessage("Status is invalid"));

    }
}
