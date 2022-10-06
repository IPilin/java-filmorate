package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Film {
    public static final int MAX_DESCRIPTION_LENGTH = 200;
    public static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
}
