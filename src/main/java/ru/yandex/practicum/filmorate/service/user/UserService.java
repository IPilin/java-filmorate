package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Map;

@Service
@Slf4j
public class UserService {
    private final UserStorage users;
    private int nextId = 1;

    @Autowired
    public UserService(InMemoryUserStorage users) {
        this.users = users;
    }

    public Map<Integer, User> getUsers() {
        return users.getAll();
    }

    public User getUser(int id) throws IncorrectIdException {
        var user = users.get(id);
        if (user == null) {
            throw new IncorrectIdException("Incorrect user ID");
        }
        return user;
    }

    public User createUser(User user) throws IncorrectIdException {
        validateUser(user);
        if (users.contains(user.getId())) {
            throw new IncorrectIdException("User's id already created.");
        }
        user.setId(nextId++);
        users.add(user);
        log.info("Created new user: " + user);
        return user;
    }

    public User changeUser(User user) throws IncorrectIdException {
        if (users.contains(user.getId())) {
            validateUser(user);
            users.add(user);
            log.info("User changed: " + user);
        } else {
            log.warn("User PUT exception: " + user);
            throw new IncorrectIdException("User doesn't exists.");
        }
        return user;
    }

    public void addFriend(int userId, int friendId) throws IncorrectIdException {
        if (userId == friendId) {
            throw new IncorrectIdException("You can't be friends with yourself :(");
        }
        var user = users.get(userId);
        var friend = users.get(friendId);
        if (user == null) {
            throw new IncorrectIdException("Incorrect user ID.");
        }
        if (friend == null) {
            throw new IncorrectIdException("Incorrect friend ID.");
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(int userId, int friendId) throws IncorrectIdException {
        if (userId == friendId) {
            throw new IncorrectIdException("You can't not be friends with yourself :(");
        }
        var user = users.get(userId);
        var friend = users.get(friendId);
        if (user == null) {
            throw new IncorrectIdException("Incorrect user ID.");
        }
        if (friend == null) {
            throw new IncorrectIdException("Incorrect friend ID.");
        }
        user.getFriends().remove(friendId);
        user.getFriends().remove(userId);
    }

    private void validateUser(User user) {
        try {
            if (!StringUtils.hasText(user.getEmail()) || !user.getEmail().contains("@")) {
                throw new ValidationException("User email is blank or wrong.");
            }
            if (!StringUtils.hasText(user.getLogin())) {
                throw new ValidationException("User login is blank.");
            }
            if (!StringUtils.hasText(user.getName())) {
                user.setName(user.getLogin());
            }
            if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("User birthday in future.");
            }
        } catch (ValidationException e) {
            log.warn("User validate exception: " + user);
            throw e;
        }
    }
}
