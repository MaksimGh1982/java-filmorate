package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan
class FilmoRateFilmTests {
    private final FilmDbStorage filmStorage;


    @Test
    public void testCreateFilm() {
        Film newFilm = new Film();
        newFilm.setName("name1");
        newFilm.setReleaseDate(LocalDate.of(1980, 12, 12));
        newFilm.setDuration(120);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.create(newFilm));
        assertThat(filmOptional).isPresent();
    }

    @Test
    public void testFindFilmById() {
        Film newFilm = new Film();
        newFilm.setName("name1");
        newFilm.setReleaseDate(LocalDate.of(1980, 12, 12));
        newFilm.setDuration(120);
        Optional<Film> newFilmOptional = Optional.ofNullable(filmStorage.create(newFilm));

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.findFilmById(newFilmOptional.get().getId()));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", newFilmOptional.get().getId())
                );
    }

    @Test
    public void testUpdateFilm() {
        Film newFilm = new Film();
        newFilm.setName("name1");
        newFilm.setReleaseDate(LocalDate.of(1980, 12, 12));
        newFilm.setDuration(120);
        Optional<Film> newFilmOptional = Optional.ofNullable(filmStorage.create(newFilm));

        Film upFilm = new Film();
        upFilm.setId(newFilmOptional.get().getId());
        upFilm.setName("UPname1");
        upFilm.setReleaseDate(LocalDate.of(1990, 12, 12));
        upFilm.setDuration(160);

        Optional<Film> upFilmOptional = Optional.ofNullable(filmStorage.update(upFilm));

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.findFilmById(newFilmOptional.get().getId()));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "UPname1")
                );
    }
}

