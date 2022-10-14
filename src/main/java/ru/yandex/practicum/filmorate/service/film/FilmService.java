package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Map;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage films;
    private int nextId = 1;

    @Autowired
    public FilmService(InMemoryFilmStorage films) {
        this.films = films;
    }

    public Map<Integer, Film> getFilms() {
        return films.getAll();
    }

    public Film getFilm(int id) throws IncorrectIdException {
        var film = films.get(id);
        if (film == null) {
            throw new IncorrectIdException("Incorrect film ID.");
        }
        return film;
    }

    public Film createFilm(Film film) throws IncorrectIdException {
        validateFilm(film);
        if (films.contains(film.getId())) {
            throw new IncorrectIdException("Film's id already created.");
        }
        film.setId(nextId++);
        films.add(film);
        log.info("Created new film: " + film);
        return film;
    }

    public Film changeFilm(Film film) throws IncorrectIdException {
        if (films.contains(film.getId())) {
            validateFilm(film);
            films.add(film);
            log.info("Film changed: " + film);
        } else {
            log.warn("Film PUT exception: " + film);
            throw new IncorrectIdException("Film id doesn't exists.");
        }
        return film;
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
            if (film.getDuration() == null || film.getDuration().isNegative()) {
                throw new ValidationException("Film duration is negative.");
            }
        } catch (ValidationException e) {
            log.warn("Film validate exception: " + film);
            throw e;
        }
    }
}
