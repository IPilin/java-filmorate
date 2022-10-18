package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws IncorrectIdException {
        return userService.createUser(user);
    }

    @PutMapping
    public User changeUser(@RequestBody User user) throws IncorrectIdException {
        return userService.changeUser(user);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") int userId) throws IncorrectIdException {
        return userService.getUser(userId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getUserFriends(@PathVariable("userId") int userId) throws IncorrectIdException {
        return userService.getUser(userId).getFriends();
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable("userId") int userId,
                          @PathVariable("friendId") int friendId) throws IncorrectIdException {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable("userId") int userId,
                             @PathVariable("friendId") int friendId) throws IncorrectIdException {
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("userId") int userId,
                                             @PathVariable("otherId") int otherId) throws IncorrectIdException {
        return userService.getCommonFriends(userId, otherId);
    }
}
