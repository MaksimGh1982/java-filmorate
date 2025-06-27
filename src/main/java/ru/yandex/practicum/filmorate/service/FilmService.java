package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> findAll() {
        log.info("Список фильмов");
        return filmStorage.findAll();
    }

    public Film findFilmById(long id) {
        log.info("Фильмов id=" + id);
        return filmStorage.findFilmById(id);
    }

    public Film create(Film film) {
        log.info("Добавить фильм");
        validate(film);
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        log.info("Обновить фильм id=" + newFilm.getId());
        validate(newFilm);
        return filmStorage.update(newFilm);
    }

    public void addLike(long filmId, long userId) {
        log.info("Лайк фильм id=" + filmId + " от пользователя id=" + userId);
        if (userService.FindUserById(userId) == null) {
            throw new NotFoundException("Пользователя id = " + userId + " не найден");
        }
        filmStorage.findFilmById(filmId).getLikes().add(userId);
    }

    public void deleteLike(long filmId, long userId) {
        log.info("удалить лайк фильму id=" + filmId + " от пользователя id=" + userId);
        if (filmStorage.findFilmById(filmId).getLikes().contains(userId)) {
            filmStorage.findFilmById(filmId).getLikes().remove(userId);
        } else {
            throw new NotFoundException("Лайк пользователя id = " + userId + " не найден");
        }
    }

    public Collection<Film> topFilm(int count) {
        log.info("Список популярных фильмов");
        return filmStorage.findAll().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
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
}
