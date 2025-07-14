package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.*;

public class FilmRowMapper implements RowMapper<Film> {
    private Map<Integer, Film> filmMap = new LinkedHashMap<>();

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {


        int filmId = resultSet.getInt("film_id");
        Film film = filmMap.get(filmId);
        if (film == null) {
            film = new Film();
            film.setId(resultSet.getLong("film_id"));
            film.setName(resultSet.getString("name"));
            film.setDescription(resultSet.getString("Description"));
            film.setReleaseDate(resultSet.getDate("releaseDate") == null ? null : resultSet.getDate("releaseDate").toLocalDate());
            film.setDuration(resultSet.getLong("duration"));

            film.getMpa().setId(resultSet.getLong("mpa_id"));
            film.getMpa().setName(resultSet.getString("mpa_name"));
            filmMap.put(filmId, film);
        }

        long genreId = resultSet.getLong("genre_id");
        if (genreId > 0) {
            Genre genre = new Genre();
            genre.setId(genreId);
            genre.setName(resultSet.getString("genre_name"));
            film.getGenres().add(genre);
        }

        return film;
    }

    public List<Film> getResult() {
        return new ArrayList<>(filmMap.values());
    }
}
