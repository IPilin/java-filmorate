package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidOperationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int usersNumber;

    @GetMapping
    public Map<Integer, User> getUsers() {
        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        validateUser(user);
        if (users.containsKey(user.getId())) {
            throw new InvalidOperationException("User's id already created.");
        }
        user.setId(++usersNumber);
        users.put(user.getId(), user);
        log.info("Created new user: " + user);
        return user;
    }

    @PutMapping
    public User changeUser(@RequestBody User user) {
        if (users.containsKey(user.getId())) {
            validateUser(user);
            users.put(user.getId(), user);
            log.info("User changed: " + user);
        } else {
            log.warn("User PUT exception: " + user);
            throw new InvalidOperationException("User doesn't exists.");
        }
        return user;
    }

    private void validateUser(User user) {
        try {
            if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                throw new ValidationException("User email is blank or wrong.");
            }
            if (user.getLogin().isBlank()) {
                throw new ValidationException("User login is blank.");
            }
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("User birthday in future.");
            }
        } catch (ValidationException e) {
            log.warn("User validate exception: " + user);
            throw e;
        }
    }
}
