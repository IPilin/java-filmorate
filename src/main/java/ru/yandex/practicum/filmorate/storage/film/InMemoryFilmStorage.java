package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.exception.IncorrectIdException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new ConcurrentHashMap<>();

    public void add(Film film) throws IncorrectIdException {
        if (contains(film.getId())) {
            log.warn("Add film error: " + film);
            throw new IncorrectIdException("Film already exists.");
        }
        films.put(film.getId(), film);
    }

    public void update(Film film) throws IncorrectIdException {
        if (!contains(film.getId())) {
            log.warn("Update film error: " + film);
            throw new IncorrectIdException("Film not found.");
        }
        films.put(film.getId(), film);
    }

    public Film get(int id) throws IncorrectIdException {
        if (!contains(id)) {
            log.warn("Film get error: " + id);
            throw new IncorrectIdException("Film not found.");
        }
        return films.get(id);
    }

    public Collection<Film> getAll() {
        return films.values();
    }

    public boolean contains(int id) {
        return films.containsKey(id);
    }
}
