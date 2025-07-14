package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Film.
 */
@Data
public class Film {
    long id;
    String name;
    String description;
    LocalDate releaseDate;
    long duration;
    Mpa mpa;
    Set<Genre> genres;

    public Film() {
        genres = new TreeSet<>(Comparator.comparing(Genre::getId));
        mpa = new Mpa();
    }
}
