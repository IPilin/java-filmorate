package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exception.IncorrectIdException;

import java.util.Collection;

@Service
public interface FilmStorage {
    void add(Film film) throws IncorrectIdException;
    void update(Film film) throws IncorrectIdException;
    Film find(int id) throws IncorrectIdException;
    Collection<Film> getAll();
    Collection<Film> getPopular(int count);
    void addLike(int filmId, User user) throws IncorrectIdException;
    void removeLike(int filmId, User user) throws IncorrectIdException;
    boolean contains(int id);
}
