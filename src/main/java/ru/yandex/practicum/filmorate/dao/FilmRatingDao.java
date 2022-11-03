package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.FilmRating;

import java.util.List;

public interface FilmRatingDao {
    FilmRating find(int id);
    List<FilmRating> findAll();
}
