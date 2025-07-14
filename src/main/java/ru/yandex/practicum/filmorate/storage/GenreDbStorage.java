package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

@Repository("GenreDbStorage")
@Slf4j
public class GenreDbStorage implements GenreStorage {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> findAll() {
        GenreRowMapper mapper = new GenreRowMapper();
        List<Genre> genres = jdbcTemplate.query("SELECT * FROM FILMORATE.GENRE", mapper);
        return genres;
    }

    @Override
    public Genre findGenreById(long id) {
        GenreRowMapper mapper = new GenreRowMapper();
        List<Genre> genres = jdbcTemplate.query("SELECT * FROM FILMORATE.GENRE where id = ?", mapper, id);

        if (genres.size() == 0) {
            return null;
        } else {
            return genres.getFirst();
        }
    }
}
