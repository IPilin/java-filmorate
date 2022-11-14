package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilmGenreDaoImpl implements FilmGenreDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmGenre find(int id) {
        String sqlQuery = "SELECT * FROM genre WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilmGenre, id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public Set<FilmGenre> findByFilmId(long filmId) {
        String sqlQuery = "SELECT * " +
                "FROM film_genre AS fg " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.id " +
                "WHERE fg.film_id = ?";
        Set<FilmGenre> result = new HashSet<>();
        var rs = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        while (rs.next()) {
            result.add(new FilmGenre(rs.getInt("genre_id"), rs.getString("name")));
        }
        return result;
    }

    @Override
    public List<FilmGenre> findAll() {
        String sqlQuery = "SELECT * FROM genre";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilmGenre);
    }

    @Override
    public void insertAllFilmGenre(Film film) {
        if (film.getGenres().isEmpty()) {
            return;
        }
        String sqlQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sqlQuery, film.getGenres(), film.getGenres().size(), (rs, genre) -> {
            rs.setLong(1, film.getId());
            rs.setLong(2, genre.getId());
        });
    }

    @Override
    public void removeAllFilmGenre(Film film) {
        String sqlQuery = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    private FilmGenre mapRowToFilmGenre(ResultSet rs, int rowNum) throws SQLException {
        return new FilmGenre(rs.getInt("id"), rs.getString("name"));
    }
}
