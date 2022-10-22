package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exception.IncorrectIdException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    public void add(User user) {
        users.put(user.getId(), user);
        log.info("User added: " + user);
    }

    public void update(User user) throws IncorrectIdException{
        if (!contains(user.getId())) {
            log.warn("Update user error: " + user);
            throw new IncorrectIdException("User not found.");
        }
        users.put(user.getId(), user);
        log.info("User changed: " + user);
    }

    public User find(int id) throws IncorrectIdException {
        if (!contains(id)) {
            log.warn("User get error: " + id);
            throw new IncorrectIdException("User not found.");
        }
        return users.get(id);
    }

    public Collection<User> getAll() {
        return users.values();
    }

    public void addFriend(int userId, int friendId) throws IncorrectIdException {
        var user = find(userId);
        var friend = find(friendId);

        user.getFriends().add(friend);
        friend.getFriends().add(user);
    }

    public void removeFriend(int userId, int friendId) throws IncorrectIdException {
        var user = find(userId);
        var friend = find(friendId);

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
    }

    public Collection<User> getCommonFriends(int user1, int user2) throws IncorrectIdException {
        var user = find(user1);
        var other = find(user2);
        return user.getFriends().stream()
                .filter(friend -> friend.getFriends().stream().anyMatch(u -> u.equals(other)))
                .collect(Collectors.toSet());
    }

    public boolean contains(int id) {
        return users.containsKey(id);
    }
}
