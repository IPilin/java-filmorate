package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Collection;

@Service
public class GenreService {
    private final FilmGenreDao filmGenreDao;

    @Autowired
    public GenreService(FilmGenreDao filmGenreDao) {
        this.filmGenreDao = filmGenreDao;
    }

    public FilmGenre find(int id) throws IncorrectIdException {
        var genre = filmGenreDao.find(id);
        if (genre == null) {
            throw new IncorrectIdException("Genre not found.");
        }
        return genre;
    }

    public Collection<FilmGenre> findAll() {
        return filmGenreDao.findAll();
    }
}
