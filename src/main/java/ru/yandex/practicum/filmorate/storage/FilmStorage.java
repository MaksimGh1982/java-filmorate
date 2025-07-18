package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;


public interface FilmStorage {

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film newFilm);

    Film findFilmById(long id);

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    Collection<Film> topFilm(int count);

}
