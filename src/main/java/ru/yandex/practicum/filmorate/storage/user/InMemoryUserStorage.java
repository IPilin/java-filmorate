package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    public void add(User user) {
        users.put(user.getId(), user);
    }

    public User get(int id) {
        return users.get(id);
    }

    public Map<Integer, User> getAll() {
        return users;
    }

    public boolean contains(int id) {
        return users.containsKey(id);
    }
}
