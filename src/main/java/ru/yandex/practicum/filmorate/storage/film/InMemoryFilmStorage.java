package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exception.IncorrectIdException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new ConcurrentHashMap<>();

    public void add(Film film) {
        films.put(film.getId(), film);
    }

    public void update(Film film) throws IncorrectIdException {
        if (!contains(film.getId())) {
            log.warn("Update film error: " + film);
            throw new IncorrectIdException("Film not found.");
        }
        films.put(film.getId(), film);
    }

    public Film find(int id) throws IncorrectIdException {
        if (!contains(id)) {
            log.warn("Film get error: " + id);
            throw new IncorrectIdException("Film not found.");
        }
        return films.get(id);
    }

    public Collection<Film> getAll() {
        return films.values();
    }

    public Collection<Film> getPopular(int count) {
        return getAll().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public void addLike(int filmId, User user) throws IncorrectIdException {
        find(filmId).getLikes().add(user);
    }

    public void removeLike(int filmId, User user) throws IncorrectIdException {
        find(filmId).getLikes().remove(user);
    }

    public boolean contains(int id) {
        return films.containsKey(id);
    }
}
