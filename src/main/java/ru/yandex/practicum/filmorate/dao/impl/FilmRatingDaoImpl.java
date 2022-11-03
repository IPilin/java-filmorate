package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmRatingDao;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FilmRatingDaoImpl implements FilmRatingDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmRatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public FilmRating find(int id) {
        String sqlQuery = "SELECT * FROM rating WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilmRating, id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<FilmRating> findAll() {
        String sqlQuery = "SELECT * FROM rating";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilmRating);
    }

    private FilmRating mapRowToFilmRating(ResultSet rs, int rowNum) throws SQLException {
        return new FilmRating(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"));
    }
}
