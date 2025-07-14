package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.time.LocalDate;
import java.util.Collection;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, UserService userService,
                       MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
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
        if (mpaStorage.findMpaById(film.getMpa().getId()) == null && film.getMpa().getId() > 0) {
            throw new NotFoundException("Рейтинг не найден");
        }
        for (Genre genre : film.getGenres()) {
            if (genreStorage.findGenreById(genre.getId()) == null && genre.getId() > 0) {
                throw new NotFoundException("Жанр не найден");
            }
        }
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        log.info("Обновить фильм id=" + newFilm.getId());
        Film oldFilm = findFilmById(newFilm.getId());
        if (oldFilm != null) {
            validate(newFilm);
            return filmStorage.update(newFilm);
        } else {
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }

    }

    public void addLike(long filmId, long userId) {

        if (userService.findUserById(userId) == null) {
            throw new NotFoundException("Пользователя id = " + userId + " не найден");
        }

        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {

        filmStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> topFilm(int count) {

        return filmStorage.topFilm(count);
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
