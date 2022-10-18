package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exception.IncorrectIdException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    public void add(User user) throws IncorrectIdException {
        if (contains(user.getId())) {
            log.warn("Add user error: " + user);
            throw new IncorrectIdException("User already exists.");
        }
        users.put(user.getId(), user);
    }

    public void update(User user) throws IncorrectIdException{
        if (!contains(user.getId())) {
            log.warn("Update user error: " + user);
            throw new IncorrectIdException("User not found.");
        }
        users.put(user.getId(), user);
    }

    public User get(int id) throws IncorrectIdException {
        if (!contains(id)) {
            log.warn("User get error: " + id);
            throw new IncorrectIdException("User not found.");
        }
        return users.get(id);
    }

    public Collection<User> getAll() {
        return users.values();
    }

    public boolean contains(int id) {
        return users.containsKey(id);
    }
}
