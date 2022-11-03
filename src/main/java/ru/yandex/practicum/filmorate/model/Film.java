package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class Film {
    public static final int MAX_DESCRIPTION_LENGTH = 200;
    public static final LocalDate FILMS_BIRTHDAY = LocalDate.of(1895, 12, 28);

    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private int rate;
    private final Set<FilmGenre> genres = new HashSet<>();
    private final Set<Long> likes = new HashSet<>();

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("rating_id", rate);
        return values;
    }
}
