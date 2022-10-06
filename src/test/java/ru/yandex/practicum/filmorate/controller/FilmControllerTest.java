package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

public class FilmControllerTest {
    FilmController filmController;
    Film film;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
        film = new Film(0, "Avengers", "d", LocalDate.now(), Duration.ofHours(2));
    }

    @Test
    public void shouldThrowExceptionIfNameIsBlank() {
        Assertions.assertAll(() -> {
            Assertions.assertEquals(film, filmController.createFilm(film));
            film.setName("");
            Assertions.assertThrows(ValidationException.class, () -> {
                filmController.createFilm(film);
            });
            film.setName("   ");
            Assertions.assertThrows(ValidationException.class, () -> {
                filmController.createFilm(film);
            });
        });
    }

    @Test
    public void shouldThrowExceptionIfDescriptionTooLong() {
        var builder = new StringBuilder();
        builder.setLength(200);
        film.setDescription(builder.toString());
        Assertions.assertAll(() -> {
            Assertions.assertEquals(film, filmController.createFilm(film));
            builder.setLength(201);
            film.setDescription(builder.toString());
            Assertions.assertThrows(ValidationException.class, () -> {
                filmController.createFilm(film);
            });
        });
    }
}
