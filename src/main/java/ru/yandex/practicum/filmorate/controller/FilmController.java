package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws IncorrectIdException {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film changeFilm(@RequestBody Film film) throws IncorrectIdException {
        return filmService.changeFilm(film);
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable("filmId") int filmId) throws IncorrectIdException {
        return filmService.getFilm(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable("filmId") int filmId,
                        @PathVariable("userId") int userId) throws IncorrectIdException {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable("filmId") int filmId,
                           @PathVariable("userId") int userId) throws IncorrectIdException {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) int count) {
        return filmService.getPopular(count);
    }
}
