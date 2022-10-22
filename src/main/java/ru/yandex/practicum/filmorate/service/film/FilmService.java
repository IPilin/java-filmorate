package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage films;
    private final UserService userService;
    private int nextId = 1;

    @Autowired
    public FilmService(InMemoryFilmStorage films, UserService userService) {
        this.films = films;
        this.userService = userService;
    }

    public Collection<Film> getFilms() {
        return films.getAll();
    }

    public Film getFilm(int id) throws IncorrectIdException {
        return films.find(id);
    }

    public Film createFilm(Film film) throws IncorrectIdException {
        validateFilm(film);
        film.setId(getNextId());
        if (films.contains(film.getId())) {
            log.warn("Add film error: " + film);
            throw new IncorrectIdException("Film already exists.");
        }
        films.add(film);
        log.info("Created new film: " + film);
        return film;
    }

    public Film changeFilm(Film film) throws IncorrectIdException {
        validateFilm(film);
        films.update(film);
        log.info("Film changed: " + film);
        return film;
    }

    public void addLike(int filmId, int userId) throws IncorrectIdException {
        films.addLike(filmId, userService.getUser(userId));
    }

    public void removeLike(int filmId, int userId) throws IncorrectIdException {
        films.removeLike(filmId, userService.getUser(userId));
    }

    public Collection<Film> getPopular(int count) {
        return films.getPopular(count);
    }

    private void validateFilm(Film film) {
        try {
            if (!StringUtils.hasText(film.getName())) {
                throw new ValidationException("Film name is blank.");
            }
            if (film.getDescription() != null && film.getDescription().length() > Film.MAX_DESCRIPTION_LENGTH) {
                throw new ValidationException("Film description is too long.");
            }
            if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(Film.FILMS_BIRTHDAY)) {
                throw new ValidationException("Film release date is before 28.12.1895");
            }
            if (film.getDuration() <= 0) {
                throw new ValidationException("Film duration is negative.");
            }
        } catch (ValidationException e) {
            log.warn("Film validate exception: " + film);
            throw e;
        }
    }

    private int getNextId() {
        return nextId++;
    }
}
