package com.cegeka.academy.web.rest.strategy;

import com.cegeka.academy.web.rest.errors.NotFoundException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationStatusContext {

    private InvitationStrategy invitationStrategy;

    private final BeanFactory beanFactory;

    @Autowired
    public InvitationStatusContext(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setInvitationStrategy(InvitationStrategy invitationStrategy) {
        this.invitationStrategy = invitationStrategy;
    }

    public void execute(Long id) throws NotFoundException {

        if (invitationStrategy.getClass().equals(AcceptInvitationStrategy.class)) {

            beanFactory.getBean(InvitationConstants.ACCEPT_INVITATION, InvitationStrategy.class).executeInvitation(id);

        } else if (invitationStrategy.getClass().equals(RejectInvitationStrategy.class)) {

            beanFactory.getBean(InvitationConstants.REJECT_INVITATION, InvitationStrategy.class).executeInvitation(id);

        }
    }
}
