package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface UserFriendDao {
    Set<Long> findUserFriends(User user);
    void insertUserFriends(User user);
    void removeUserFriends(User user);
    void addUserFriend(User user, User friend);
    void removeUserFriend(User user, User friend);
}
