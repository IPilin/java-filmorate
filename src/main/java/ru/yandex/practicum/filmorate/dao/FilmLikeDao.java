package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface FilmLikeDao {
    Set<Long> findFilmLikes(long filmId);
    void insertAllFilmLike(Film film);
    void removeAllFilmLike(Film film);
    void addFilmLike(Film film, User user);
    void removeFilmLike(Film film, User user);
}
