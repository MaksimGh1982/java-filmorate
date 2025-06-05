package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Список фильмов");
        return films.values();
    }

    public void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название должно быть указано");
            throw new ValidationException("Название должно быть указано");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("максимальная длина описания — 200 символов");
            throw new ValidationException("максимальная длина описания — 200 символов");
        }

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("дата релиза — раньше 28 декабря 1895 года");
            throw new ValidationException("дата релиза — раньше 28 декабря 1895 года");
        }

        if (film.getDuration() < 0) {
            log.error("продолжительность фильма должна быть положительным числом");
            throw new ValidationException("продолжительность фильма должна быть положительным числом");
        }
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Добавить фильм");
        // проверяем выполнение необходимых условий
        validate(film);

        film.setId(getNextId());

        films.put(film.getId(), film);
        log.debug("Добавлен фильм", film);
        return film;
    }

    // вспомогательный метод для генерации идентификатора
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Обновить фильм id=" + newFilm.getId());
        validate(newFilm);

        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            oldFilm.setName(newFilm.getName());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(Duration.ofSeconds(newFilm.getDuration()));
            log.debug("Изменен фильм", oldFilm);
            return oldFilm;
        }
        throw new ValidationException("Фильм с id = " + newFilm.getId() + " не найден");
    }
}
