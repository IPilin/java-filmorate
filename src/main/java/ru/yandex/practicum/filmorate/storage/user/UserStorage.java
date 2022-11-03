package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exception.IncorrectIdException;

import java.util.Collection;

public interface UserStorage {
    void add(User user) throws IncorrectIdException;
    void update(User user) throws IncorrectIdException;
    User find(long id) throws IncorrectIdException;
    Collection<User> getAll();
    void addFriend(long userId, long friendId) throws IncorrectIdException;
    void removeFriend(long userId, long friendId) throws IncorrectIdException;
    Collection<User> getCommonFriends(long user1, long user2) throws IncorrectIdException;
    boolean contains(long id);
}
