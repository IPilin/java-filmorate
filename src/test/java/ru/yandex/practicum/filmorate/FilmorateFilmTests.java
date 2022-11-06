package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmorateFilmTests {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    private static Film filmOne;
    private static Film filmTwo;

    @BeforeAll
    public static void beforeAll() {
        filmOne = Film.builder()
                .name("Avengers")
                .description("avengers movie")
                .releaseDate(LocalDate.of(2012, 10, 12))
                .duration(120)
                .mpa(new FilmRating(2, "PG", "детям рекомендуется смотреть фильм с родителями"))
                .build();
        filmTwo = Film.builder()
                .name("Iron Men")
                .description("men from iron...")
                .releaseDate(LocalDate.of(208, 9, 12))
                .duration(120)
                .mpa(new FilmRating(2, "PG", "детям рекомендуется смотреть фильм с родителями"))
                .build();
    }

    @Test
    @Order(1)
    public void createFilm() throws IncorrectIdException {
        filmStorage.add(filmOne);

        assertThat(filmStorage.find(filmOne.getId())).isEqualTo(filmOne);
    }

    @Test
    @Order(2)
    public void changeFilm() throws IncorrectIdException {
        filmOne.setDescription("new description");
        filmStorage.update(filmOne);

        assertThat(filmStorage.find(filmOne.getId())).isEqualTo(filmOne);
    }

    @Test
    @Order(3)
    public void setAndRemoveFilmGenre() throws IncorrectIdException {
        filmOne.getGenres().add(new FilmGenre(2, "Драма"));
        filmOne.getGenres().add(new FilmGenre(6, "Боевик"));

        filmStorage.update(filmOne);

        assertThat(filmStorage.find(filmOne.getId())).isEqualTo(filmOne);

        filmOne.getGenres().clear();

        filmStorage.update(filmOne);

        assertThat(filmStorage.find(filmOne.getId()).getGenres()).isEmpty();
    }

    @Test
    @Order(4)
    public void addAndRemoveLikes() throws IncorrectIdException {
        filmStorage.add(filmTwo);

        filmStorage.addLike(filmOne.getId(), userStorage.find(1));
        filmStorage.addLike(filmTwo.getId(), userStorage.find(1));

        assertThat(filmStorage.find(filmOne.getId()).getLikes()).contains(1L);
        assertThat(filmStorage.find(filmTwo.getId()).getLikes()).contains(1L);

        filmOne = filmStorage.find(filmOne.getId());
        filmStorage.removeLike(filmTwo.getId(), userStorage.find(1));

        assertThat(filmStorage.find(filmTwo.getId()).getLikes()).isEmpty();
    }

    @Test
    @Order(5)
    public void getPopularFilms() {
        var populars = filmStorage.getPopular(1);

        assertThat(populars.size()).isEqualTo(1);
        assertThat(populars).contains(filmOne);

        populars = filmStorage.getPopular(10);

        assertThat(populars.size()).isEqualTo(2);
        assertThat(populars).first().isEqualTo(filmOne);
        assertThat(populars).last().isEqualTo(filmTwo);
    }
}
