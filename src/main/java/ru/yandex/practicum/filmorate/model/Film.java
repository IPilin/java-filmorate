package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    public static final int MAX_DESCRIPTION_LENGTH = 200;
    public static final LocalDate FILMS_BIRTHDAY = LocalDate.of(1895, 12, 28);

    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
}
