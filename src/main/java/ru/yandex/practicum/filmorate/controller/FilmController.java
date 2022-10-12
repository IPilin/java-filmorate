package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidOperationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new ConcurrentHashMap<>();
    private int filmsNumber;

    @GetMapping
    public Map<Integer, Film> getFilms() {
        return films;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws InvalidOperationException {
        validateFilm(film);
        if (films.containsKey(film.getId())) {
            throw new InvalidOperationException("Film's id already created.");
        }
        film.setId(++filmsNumber);
        films.put(film.getId(), film);
        log.info("Created new film: " + film);
        return film;
    }

    @PutMapping
    public Film changeFilm(@RequestBody Film film) throws InvalidOperationException {
        if (films.containsKey(film.getId())) {
            validateFilm(film);
            films.put(film.getId(), film);
            log.info("Film changed: " + film);
        } else {
            log.warn("Film PUT exception: " + film);
            throw new InvalidOperationException("Film id doesn't exists.");
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
