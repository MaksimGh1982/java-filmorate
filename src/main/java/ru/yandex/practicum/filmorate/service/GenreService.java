package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.text.MessageFormat;
import java.util.Collection;

@Service
@Slf4j
public class GenreService {
    @Autowired
    private GenreStorage genreStorage;

    public Collection<Genre> findAll() {
        log.info("Список жанров");
        return genreStorage.findAll();
    }

    public Genre findGenreById(long id) {
        log.info("Жанр id = {}", id);
        Genre genre = genreStorage.findGenreById(id);
        if (genre == null) {
            throw new NotFoundException(MessageFormat.format("Жанр id = {0} не найден", id));
        } else {
            return genre;
        }
    }
}
