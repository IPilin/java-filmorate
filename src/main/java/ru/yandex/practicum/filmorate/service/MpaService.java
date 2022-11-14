package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmRatingDao;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.FilmRating;

import java.util.Collection;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MpaService {
    private final FilmRatingDao filmRatingDao;

    public FilmRating find(int mpaId) throws IncorrectIdException {
        var rating = filmRatingDao.find(mpaId);
        if (rating == null) {
            throw new IncorrectIdException("Mpa not found.");
        }
        return rating;
    }

    public Collection<FilmRating> findAll() {
        return filmRatingDao.findAll();
    }
}
