package com.kc.gitrepo.controller;

import com.kc.gitrepo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/git")
public class GitHubController {

    @Autowired
    private UserService userService;


    @GetMapping
    public Flux<?> findAllUsers() {
        return userService.findAllUsers();
    }


}
