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
    public Map<Integer, Film> getFilms() {
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
}
