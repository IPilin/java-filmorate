package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exception.IncorrectIdException;

import java.util.Collection;

public interface UserStorage {
    void add(User user) throws IncorrectIdException;
    void update(User user) throws IncorrectIdException;
    User get(int id) throws IncorrectIdException;
    Collection<User> getAll();
    boolean contains(int id);
}