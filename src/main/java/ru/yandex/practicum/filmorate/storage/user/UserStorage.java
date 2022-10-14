package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    void add(User user);
    User get(int id);
    Map<Integer, User> getAll();
    boolean contains(int id);
}
