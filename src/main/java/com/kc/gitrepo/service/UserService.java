package com.kc.gitrepo.service;

import com.kc.gitrepo.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    public Flux<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        User user1 = User.builder()
                .id(UUID.randomUUID().toString())
                .name("Khemchand Joshi")
                .build();
        users.add(user1);
        return Mono.just(users).flatMapMany(Flux::fromIterable);
    }
}
