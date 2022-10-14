package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new ConcurrentHashMap<>();

    public void add(Film film) {
        films.put(film.getId(), film);
    }

    public Film get(int id) {
        return films.get(id);
    }

    public Map<Integer, Film> getAll() {
        return films;
    }

    public boolean contains(int id) {
        return films.containsKey(id);
    }
}
