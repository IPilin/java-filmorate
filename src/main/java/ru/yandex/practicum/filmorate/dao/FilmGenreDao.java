package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;
import java.util.Set;

public interface FilmGenreDao {
    FilmGenre find(int id);
    Set<FilmGenre> findByFilmId(long filmId);
    List<FilmGenre> findAll();
    void insertAllFilmGenre(Film film);
    void removeAllFilmGenre(Film film);
}
