package com.cegeka.academy.web.rest;

import com.cegeka.academy.domain.UserChallenge;
import com.cegeka.academy.domain.enums.InvitationStatus;
import com.cegeka.academy.service.dto.ChallengeDTO;
import com.cegeka.academy.service.dto.UserChallengeDTO;
import com.cegeka.academy.service.userChallenge.UserChallengeService;
import com.cegeka.academy.web.rest.errors.InvalidInvitationStatusException;
import com.cegeka.academy.web.rest.errors.InvalidUserChallengeStatusException;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import com.cegeka.academy.web.rest.errors.WrongOwnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userChallenge")
public class UserChallengeController {

    @Autowired
    private UserChallengeService userChallengeService;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public List<UserChallengeDTO> getChallengesByUserId(@PathVariable("userId") Long userId) throws NotFoundException {

        return userChallengeService.getUserChallengesByUserId(userId);
    }

    @PutMapping("/updateStatus/{userChallengeId}/{status}")
    public ResponseEntity<String> updateUserChallengeStatus(@PathVariable(value = "userChallengeId") Long userChallengeId, @PathVariable(value = "status") String status) throws NotFoundException, InvalidUserChallengeStatusException {

        userChallengeService.updateUserChallengeStatus(userChallengeId, status);

        return ResponseEntity.ok("User challenge status was updated.");

    }

    @PutMapping("/updateInvitationStatus/{userChallengeId}/{invitationStatus}")
    public ResponseEntity<String> updateUserChallengeInvitationStatus(@PathVariable(value = "userChallengeId") Long userChallengeId, @PathVariable(value = "invitationStatus") String invitationStatus) throws NotFoundException, InvalidInvitationStatusException {

        userChallengeService.updateUserChallengeInvitationStatus(userChallengeId, invitationStatus);

        return ResponseEntity.ok("User challenge invitation status was updated.");

    }

    @PutMapping(value = "/rate/{ownerId}")
    public UserChallenge rateUser(@RequestBody UserChallengeDTO userChallengeDTO,
                                  @PathVariable("ownerId") Long ownerId) throws WrongOwnerException {

        return userChallengeService.rateUser(userChallengeDTO, ownerId);
    }

    @GetMapping("/next/{userId}")
    public List<ChallengeDTO> getNextChallenges(@PathVariable(value = "userId") Long userId) throws NotFoundException {

        return userChallengeService.getNextChallengesForAnUser(userId);

    }

    @GetMapping("/pending/{userId}")
    public List<ChallengeDTO> getChallengesWithPendingInvitation(@PathVariable(value = "userId") Long userId) throws NotFoundException {

        return userChallengeService.getChallengesByInvitationStatus(userId, InvitationStatus.PENDING);

    }

}
