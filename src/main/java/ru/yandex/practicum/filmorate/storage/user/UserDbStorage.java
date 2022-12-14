package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserFriendDao;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserFriendDao userFriendDao;

    @Override
    public void add(User user) throws IncorrectIdException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("filmorate_users")
                .usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        userFriendDao.insertUserFriends(user);
    }

    @Override
    public void update(User user) throws IncorrectIdException {
        if (!contains(user.getId())) {
            log.warn("User update error: " + user);
            throw new IncorrectIdException("User not found.");
        }
        userFriendDao.removeUserFriends(user);
        String sqlQuery = "UPDATE filmorate_users SET " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        userFriendDao.insertUserFriends(user);
    }

    @Override
    public User find(long id) throws IncorrectIdException {
        String sqlQuery = "SELECT * FROM filmorate_users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } catch (DataAccessException e) {
            throw new IncorrectIdException("User not found.");
        }
    }

    @Override
    public Collection<User> getAll() {
        String sqlQuery = "SELECT * FROM filmorate_users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    public Collection<User> getFriends(long userId) throws IncorrectIdException {
        var user = find(userId);
        Set<User> friends = new HashSet<>();
        for (Long friendId : user.getFriends()) {
            friends.add(find(friendId));
        }
        return friends;
    }

    @Override
    public void addFriend(long userId, long friendId) throws IncorrectIdException {
        var user = find(userId);
        var friend = find(friendId);
        if (friend.getFriends().contains(user.getId())) {
            return;
        }
        friend.getFriends().add(user.getId());
        userFriendDao.addUserFriend(friend, user);
    }

    @Override
    public void removeFriend(long userId, long friendId) throws IncorrectIdException {
        var user = find(userId);
        var friend = find(friendId);

        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());

        userFriendDao.removeUserFriend(user, friend);
        userFriendDao.removeUserFriend(friend, user);
    }

    @Override
    public Collection<User> getCommonFriends(long user1, long user2) throws IncorrectIdException {
        var userOne = find(user1);
        var userTwo = find(user2);
        Set<Long> commonFriendsId = new HashSet<>();
        for (Long i : userOne.getFriends()) {
            for (Long j : userTwo.getFriends()) {
                if (Objects.equals(i, j)) {
                    commonFriendsId.add(i);
                }
            }
        }
        Set<User> result = new HashSet<>();
        for (Long friendId : commonFriendsId) {
            result.add(find(friendId));
        }
        return result;
    }

    @Override
    public boolean contains(long id) {
        String sqlQuery = "SELECT * FROM filmorate_users WHERE id = ?";
        return jdbcTemplate.queryForRowSet(sqlQuery, id).next();
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        var user = User.builder().id(rs.getLong("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
        user.getFriends().addAll(userFriendDao.findUserFriends(user));
        return user;
    }
}
