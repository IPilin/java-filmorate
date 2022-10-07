package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        assertAll(
                () ->  {
                    assertEquals(film, filmController.createFilm(film));
                },
                () -> {
                    film.setName("");
                    assertThrows(ValidationException.class, () -> filmController.createFilm(film));
                },
                () -> {
                    film.setName("   ");
                    assertThrows(ValidationException.class, () -> filmController.createFilm(film));
                }
        );
    }

    @Test
    public void shouldThrowExceptionIfDescriptionTooLong() {
        var builder = new StringBuilder();
        assertAll(
                () -> {
                    builder.setLength(200);
                    film.setDescription(builder.toString());
                    assertEquals(film, filmController.createFilm(film));
                },
                () -> {
                    builder.setLength(201);
                    film.setDescription(builder.toString());
                    assertThrows(ValidationException.class, () -> filmController.createFilm(film));
                },
                () -> {
                    builder.setLength(1000);
                    film.setDescription(builder.toString());
                    assertThrows(ValidationException.class, () -> filmController.createFilm(film));
                }
        );
    }

    @Test
    public void shouldThrowExceptionIfReleaseDateIsTooYoung() {
        assertAll(
                () -> {
                    film.setReleaseDate(Film.FILMS_BIRTHDAY.minusDays(1));
                    assertThrows(ValidationException.class, () -> filmController.createFilm(film));
                },
                () -> {
                    film.setReleaseDate(Film.FILMS_BIRTHDAY);
                    assertEquals(film, filmController.createFilm(film));
                }
        );
    }

    @Test
    public void shouldThrowExceptionIfDurationIsNegative() {
        assertAll(
                () -> {
                    film.setDuration(Duration.ZERO.minusNanos(1));
                    assertThrows(ValidationException.class, () -> filmController.createFilm(film));
                },
                () -> {
                    film.setDuration(Duration.ZERO);
                    assertEquals(film, filmController.createFilm(film));
                }
        );
    }
}
