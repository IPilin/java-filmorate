package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmRatingDao;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.util.Collection;

@Service
public class MpaService {
    private final FilmRatingDao filmRatingDao;

    @Autowired
    public MpaService(FilmRatingDao filmRatingDao) {
        this.filmRatingDao = filmRatingDao;
    }

    public FilmRating find(int id) throws IncorrectIdException {
        var rating = filmRatingDao.find(id);
        if (rating == null) {
            throw new IncorrectIdException("Mpa not found.");
        }
        return rating;
    }

    public Collection<FilmRating> findAll() {
        return filmRatingDao.findAll();
    }
}
