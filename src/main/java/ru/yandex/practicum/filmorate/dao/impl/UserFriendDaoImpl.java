package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserFriendDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

@Repository
public class UserFriendDaoImpl implements UserFriendDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserFriendDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Set<Long> findUserFriends(User user) {
        Set<Long> friends = new HashSet<>();
        String sqlQuery = "SELECT friend_id FROM user_friend WHERE user_id = ?";
        var rs = jdbcTemplate.queryForRowSet(sqlQuery, user.getId());
        while (rs.next()) {
            friends.add(rs.getLong("friend_id"));
        }
        return friends;
    }

    @Override
    public void insertUserFriends(User user) {
        if (user.getFriends().isEmpty()) {
            return;
        }
        String sqlQuery = "INSERT INTO user_friend (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sqlQuery, user.getFriends(), user.getFriends().size(), (rs, friendId) -> {
            rs.setLong(1, user.getId());
            rs.setLong(2, friendId);
        });
    }

    @Override
    public void removeUserFriends(User user) {
        String sqlQuery = "DELETE FROM user_friend WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getId());
    }

    @Override
    public void addUserFriend(User user, User friend) {
        String sqlQuery = "INSERT INTO user_friend (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, user.getId(), friend.getId());
    }

    @Override
    public void removeUserFriend(User user, User friend) {
        String sqlQuery = "DELETE FROM user_friend WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, user.getId(), friend.getId());
    }
}
