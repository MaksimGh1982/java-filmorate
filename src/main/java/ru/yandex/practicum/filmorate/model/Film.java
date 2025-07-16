package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Film.
 */
@Data
public class Film {
    long id;
    @NotBlank(message = "Название должно быть указано")
    String name;
    @Size(min = 1, max = 200, message = "Описание должно содержать до 200 символов")
    String description;
    LocalDate releaseDate;
    @Min(value = 0, message = "продолжительность фильма должна быть положительным числом")
    int duration;
    Mpa mpa;
    Set<Genre> genres;

    public Film() {
        genres = new TreeSet<>(Comparator.comparing(Genre::getId));
        mpa = new Mpa();
    }
}
