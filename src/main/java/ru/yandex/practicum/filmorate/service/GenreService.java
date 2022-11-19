package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Collection;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GenreService {
    private final FilmGenreDao filmGenreDao;

    public FilmGenre find(int genreId) throws IncorrectIdException {
        var genre = filmGenreDao.find(genreId);
        if (genre == null) {
            throw new IncorrectIdException("Genre not found.");
        }
        return genre;
    }

    public Collection<FilmGenre> findAll() {
        return filmGenreDao.findAll();
    }
}
