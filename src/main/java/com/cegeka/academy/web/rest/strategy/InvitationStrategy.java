package com.cegeka.academy.web.rest.strategy;

import com.cegeka.academy.web.rest.errors.NotFoundException;

public interface InvitationStrategy {

    void executeInvitation(Long id) throws NotFoundException;

}
