package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exception.IncorrectIdException;

import java.util.Collection;

public interface UserStorage {
    void add(User user) throws IncorrectIdException;
    void update(User user) throws IncorrectIdException;
    User find(int id) throws IncorrectIdException;
    Collection<User> getAll();
    void addFriend(int userId, int friendId) throws IncorrectIdException;
    void removeFriend(int userId, int friendId) throws IncorrectIdException;
    Collection<User> getCommonFriends(int user1, int user2) throws IncorrectIdException;
    boolean contains(int id);
}
