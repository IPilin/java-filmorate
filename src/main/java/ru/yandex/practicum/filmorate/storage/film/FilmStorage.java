package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.exception.IncorrectIdException;

import java.util.Collection;

@Service
public interface FilmStorage {
    void add(Film film) throws IncorrectIdException;
    void update(Film film) throws IncorrectIdException;
    Film get(int id) throws IncorrectIdException;
    Collection<Film> getAll();
    boolean contains(int id);
}
