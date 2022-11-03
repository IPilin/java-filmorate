package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

@Repository
public class FilmLikeDaoImpl implements FilmLikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmLikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Long> findFilmLikes(long filmId) {
        Set<Long> result = new HashSet<>();
        String sqlQuery = "SELECT user_id FROM film_like WHERE film_id = ?";
        var rs = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        while (rs.next()) {
            result.add(rs.getLong("user_id"));
        }
        return result;
    }

    @Override
    public void insertAllFilmLike(Film film) {
        if (film.getLikes().isEmpty()) {
            return;
        }
        String sqlQuery = "INSERT INTO film_like (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sqlQuery, film.getLikes(), film.getLikes().size(), (rs, userId) -> {
            rs.setLong(1, film.getId());
            rs.setLong(2, userId);
        });
    }

    @Override
    public void removeAllFilmLike(Film film) {
        String sqlQuery = "DELETE FROM film_like WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    @Override
    public void addFilmLike(Film film, User user) {
        String sqlQuery = "INSERT INTO film_like (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }

    @Override
    public void removeFilmLike(Film film, User user) {
        String sqlQuery = "DELETE FROM film_like WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }
}
