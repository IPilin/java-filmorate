package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.dao.impl.FilmGenreDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreDao filmGenreDao;

    private final FilmLikeDao filmLikeDao;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         FilmGenreDaoImpl filmGenreDao,
                         FilmLikeDao filmLikeDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenreDao = filmGenreDao;
        this.filmLikeDao = filmLikeDao;
    }

    @Override
    public void add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("filmorate_films")
                .usingGeneratedKeyColumns("id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue());
        filmGenreDao.insertAllFilmGenre(film);
        filmLikeDao.insertAllFilmLike(film);
    }

    @Override
    public void update(Film film) throws IncorrectIdException {
        if (!contains(film.getId())) {
            log.warn("Film update error: " + film);
            throw new IncorrectIdException("Film not found.");
        }
        filmGenreDao.removeAllFilmGenre(film);
        filmLikeDao.removeAllFilmLike(film);
        String sqlQuery = "UPDATE filmorate_films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getRate(),
                film.getId());
        filmGenreDao.insertAllFilmGenre(film);
        filmLikeDao.insertAllFilmLike(film);
    }

    @Override
    public Film find(long id) throws IncorrectIdException {
        String sqlQuery = "SELECT * " +
                "FROM filmorate_films " +
                "WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (DataAccessException e) {
            throw new IncorrectIdException("Film not found.");
        }
    }

    @Override
    public Collection<Film> getAll() {
        String sqlQuery = "SELECT * " +
                "FROM filmorate_films ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Collection<Film> getPopular(int count) {
        String sqlQuery = "SELECT id, name, description, release_date, duration, rating_id, COUNT(film_id) " +
                "FROM filmorate_films " +
                "LEFT JOIN film_like ON film_id = id " +
                "GROUP BY id " +
                "ORDER BY COUNT(film_id) DESC " +
                "LIMIT ? ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    @Override
    public void addLike(long filmId, User user) throws IncorrectIdException {
        var film = find(filmId);
        if (film.getLikes().contains(user.getId())) {
            return;
        }
        film.getLikes().add(user.getId());
        filmLikeDao.addFilmLike(film, user);
    }

    @Override
    public void removeLike(long filmId, User user) throws IncorrectIdException {
        var film = find(filmId);
        film.getLikes().remove(user.getId());
        filmLikeDao.removeFilmLike(film, user);
    }

    @Override
    public boolean contains(long id) {
        String sqlQuery = "SELECT * FROM filmorate_films WHERE id = ?";
        return jdbcTemplate.queryForRowSet(sqlQuery, id).next();
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        var film = Film.builder().id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rating_id"))
                .build();

        film.getGenres().addAll(filmGenreDao.findByFilmId(film.getId()));
        film.getLikes().addAll(filmLikeDao.findFilmLikes(film.getId()));
        return film;
    }
}
