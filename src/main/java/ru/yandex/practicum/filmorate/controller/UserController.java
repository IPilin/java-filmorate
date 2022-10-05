package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Set<User> users = new HashSet<>();

    @GetMapping
    public Set<User> getUsers() {
        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        users.add(user);
        return user;
    }

    @PutMapping
    public User changeUser(@RequestBody User user) {
        //TODO: must be done by rewriting hashcode
        return user;
    }
}
