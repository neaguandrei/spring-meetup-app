package com.cegeka.academy.web.rest;

import com.cegeka.academy.service.challenge.ChallengeService;
import com.cegeka.academy.service.dto.UserChallengeDTO;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import org.springframework.web.bind.annotation.*;
import com.cegeka.academy.service.dto.ChallengeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ChallengeDTO getChallengeById(@PathVariable long id) throws NotFoundException {
        return challengeService.getChallengeById(id);
    }


    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) throws NotFoundException {
        challengeService.deleteChallenge(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ChallengeDTO updateChallenge(@Valid @RequestBody ChallengeDTO challengeDTO, @PathVariable(value ="id") Long challengeId) throws NotFoundException {
        return challengeService.updateChallenge(challengeId, challengeDTO);
    }

    @GetMapping(path = "/user/{id}")
    public Set<ChallengeDTO> getUserChallengesByUserId(@PathVariable long id) throws NotFoundException {
        return challengeService.getChallengesByUserId(id);
    }


    @PostMapping
    public void saveChallenge(@Valid @RequestBody ChallengeDTO challenge) {

        challengeService.saveChallenge(challenge);

    }

    @GetMapping("/creator/{creatorId}")
    public List<ChallengeDTO> getChallengesByCreatorId(@PathVariable(value = "creatorId") Long creatorId) throws NotFoundException {

        return challengeService.getChallengesByCreatorId(creatorId);

    }


    @GetMapping("/public")
    public List<ChallengeDTO> getPublicChallenges() throws NotFoundException {

        return challengeService.getPublicChallenges();

    }

    @GetMapping("/rank/{challengeId}")
    public List<UserChallengeDTO> getChallengeRankById(@PathVariable Long challengeId, @RequestParam(value = "orderBy") String orderBy) throws NotFoundException {
        return challengeService.getChallengeRanking(challengeId, orderBy);
    }
}
