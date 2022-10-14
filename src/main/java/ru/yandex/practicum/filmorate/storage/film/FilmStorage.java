package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

@Service
public interface FilmStorage {
    void add(Film film);
    Film get(int id);
    Map<Integer, Film> getAll();
    boolean contains(int id);
}
