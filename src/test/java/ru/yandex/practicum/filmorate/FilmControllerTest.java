package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {

    private static FilmService filmService;

    @BeforeAll
    static void beforeAll() {
        filmService = new FilmService(new InMemoryFilmStorage(), new UserService(new InMemoryUserStorage()),
                new MpaDbStorage(), new GenreDbStorage());
    }

    @Test
    void newOldestFilm() {
        Film film = new Film();
        film.setName("Titanic");
        film.setReleaseDate(LocalDate.of(1800, 12, 12));
        film.setDuration(180);
        assertThrows(ValidationException.class, () -> {
            filmService.create(film);
        }, "ValidationException was expected");
    }

    @Test
    void newEmptyFilm() {
        Film film = new Film();
        assertThrows(ValidationException.class, () -> {
            filmService.create(film);
        }, "ValidationException was expected");
    }

}
