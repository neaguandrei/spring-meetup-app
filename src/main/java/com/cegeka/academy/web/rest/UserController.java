package com.cegeka.academy.web.rest;

import com.cegeka.academy.service.UserService;
import com.cegeka.academy.service.dto.UserDTO;
import com.cegeka.academy.web.rest.errors.InvalidArgumentsException;
import com.cegeka.academy.web.rest.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search/{keyword}")
    public List<String> searchByKeyword(@PathVariable String keyword) throws NotFoundException {
        return userService.searchByKeyword(keyword).stream().sorted().collect(Collectors.toList());
    }

    @GetMapping("/{firstName}/{lastName}")
    public List<UserDTO> getAllUsersByFirstAndLastName(@PathVariable String firstName, @PathVariable String lastName) throws NotFoundException, InvalidArgumentsException {

        return userService.findUsersByFirstAndLastName(firstName, lastName);
    }

    @GetMapping("/category/{categoryName}")
    public List<UserDTO> getAllUsersByCategoryName(@PathVariable String categoryName) throws NotFoundException {

        return userService.findByInterestedCategoryName(categoryName);
    }
}
