package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Set<Film> films = new HashSet<>();

    @GetMapping
    public Set<Film> getFilms() {
        return films;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        films.add(film);
        return film;
    }

    @PutMapping
    public Film changeFilm(@RequestBody Film film) {
        //TODO: must be done by rewriting hashcode
        return film;
    }
}
