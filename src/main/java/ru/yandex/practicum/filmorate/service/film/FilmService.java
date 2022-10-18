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
import java.util.stream.Collectors;

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
        return films.get(id);
    }

    public Film createFilm(Film film) throws IncorrectIdException {
        validateFilm(film);
        film.setId(nextId++);
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
        films.get(filmId).getLikes().add(userService.getUser(userId));
    }

    public void removeLike(int filmId, int userId) throws IncorrectIdException {
        films.get(filmId).getLikes().remove(userService.getUser(userId));
    }

    public Collection<Film> getPopular(int count) {
        return films.getAll().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
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
}
