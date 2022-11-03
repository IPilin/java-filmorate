package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exception.IncorrectIdException;

import java.util.Collection;

@Service
public interface FilmStorage {
    void add(Film film);
    void update(Film film) throws IncorrectIdException;
    Film find(long id) throws IncorrectIdException;
    Collection<Film> getAll();
    Collection<Film> getPopular(int count);
    void addLike(long filmId, User user) throws IncorrectIdException;
    void removeLike(long filmId, User user) throws IncorrectIdException;
    boolean contains(long id);
}
