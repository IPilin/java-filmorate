package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

public class FilmControllerTest {
    FilmController filmController;
    Film film;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new UserService(new InMemoryUserStorage())));
        film = new Film(0, "Avengers", "d", LocalDate.now(), 100);
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
                    film.setDuration(0);
                    assertThrows(ValidationException.class, () -> filmController.createFilm(film));
                },
                () -> {
                    film.setDuration(-120);
                    assertEquals(film, filmController.createFilm(film));
                }
        );
    }
}
