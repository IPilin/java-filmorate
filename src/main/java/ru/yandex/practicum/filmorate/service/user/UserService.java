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
import java.util.Collection;

@Service
@Slf4j
public class UserService {
    private final UserStorage users;
    private int nextId = 1;

    @Autowired
    public UserService(InMemoryUserStorage users) {
        this.users = users;
    }

    public Collection<User> getUsers() {
        return users.getAll();
    }

    public User getUser(int id) throws IncorrectIdException {
        return users.find(id);
    }

    public User createUser(User user) throws IncorrectIdException {
        validateUser(user);
        user.setId(getNextId());
        if (users.contains(user.getId())) {
            log.warn("Add user error: " + user);
            throw new IncorrectIdException("User already exists.");
        }
        users.add(user);
        return user;
    }

    public User changeUser(User user) throws IncorrectIdException {
        validateUser(user);
        users.update(user);
        return user;
    }

    public void addFriend(long userId, long friendId) throws IncorrectIdException {
        if (userId == friendId) {
            throw new IncorrectIdException("You can't be friends with yourself :(");
        }
        users.addFriend(userId, friendId);
    }

    public void removeFriend(long userId, long friendId) throws IncorrectIdException {
        if (userId == friendId) {
            throw new IncorrectIdException("You can't not be friends with yourself :(");
        }
        users.removeFriend(userId, friendId);
    }

    public Collection<User> getCommonFriends(long userId, long otherId) throws IncorrectIdException {
        return users.getCommonFriends(userId, otherId);
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

    private int getNextId() {
        return nextId++;
    }
}
