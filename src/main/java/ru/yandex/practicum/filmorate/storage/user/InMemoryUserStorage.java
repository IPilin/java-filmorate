package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new ConcurrentHashMap<>();

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

    public User find(long id) throws IncorrectIdException {
        if (!contains(id)) {
            log.warn("User get error: " + id);
            throw new IncorrectIdException("User not found.");
        }
        return users.get(id);
    }

    public Collection<User> getAll() {
        return users.values();
    }

    public Collection<User> getFriends(long userId) throws IncorrectIdException {
        var user = find(userId);
        Set<User> friends = new HashSet<>();
        for (Long friendId : user.getFriends()) {
            friends.add(find(friendId));
        }
        return friends;
    }

    public void addFriend(long userId, long friendId) throws IncorrectIdException {
        var user = find(userId);
        var friend = find(friendId);

        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }

    public void removeFriend(long userId, long friendId) throws IncorrectIdException {
        var user = find(userId);
        var friend = find(friendId);

        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    public Collection<User> getCommonFriends(long user1, long user2) throws IncorrectIdException {
        return null;
    }

    public boolean contains(long id) {
        return users.containsKey(id);
    }
}
