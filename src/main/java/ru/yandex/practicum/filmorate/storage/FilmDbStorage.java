package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;

@Repository("FilmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    @Qualifier("UserDbStorage")
    UserStorage userStorage;
    @Autowired
    GenreStorage genreStorage;
    @Autowired
    MpaStorage mpaStorage;

    @Override
    public Film create(Film film) {

        String sqlQuery = "INSERT INTO FILMORATE.FILM (NAME, description, releaseDate, Duration, MPA_Id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(
                    "INSERT INTO FILMORATE.FilmGenre (FilmId, GenreId) " +
                            "VALUES (?, ?)",
                    filmId, genre.getId());
        }

        log.debug("Добавлен фильм", film);
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        log.debug("Список всех фильмов");
        FilmRowMapper mapper = new FilmRowMapper();
        jdbcTemplate.query("SELECT f.ID film_id,f.NAME,f.DESCRIPTION,f.RELEASEDATE,f.DURATION, " +
                                    "m.id mpa_id,m.NAME mpa_name,g.ID genre_id,g.NAME genre_name, " +
                                 "FROM FILMORATE.FILM f " +
                                     "LEFT JOIN FILMORATE.МРА m ON f.MPA_ID=m.id " +
                                     "LEFT JOIN FILMORATE.FILMGENRE fg ON fg.filmid=f.id " +
                                     "LEFT JOIN FILMORATE.GENRE g ON fg.genreid=g.id order by f.id,g.ID", mapper);
        List<Film> films = mapper.getResult();
        return films;
    }

    @Override
    public Film update(Film newFilm) {

        jdbcTemplate.update(
                "UPDATE FILMORATE.FILM set NAME=?, description=?, releaseDate=?, Duration=?, MPA_Id=? where id=? ",
                newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(), newFilm.getDuration(), newFilm.getMpa().getId(), newFilm.getId());
        jdbcTemplate.update(
                "delete from FILMORATE.FilmGenre where filmId=? ", newFilm.getId());

        for (Genre genre : newFilm.getGenres()) {
            jdbcTemplate.update(
                    "INSERT INTO FILMORATE.FilmGenre (FilmId, GenreId) " +
                            "VALUES (?, ?)",
                    newFilm.getId(), genre.getId());
        }
        log.debug("Обновлен фильм", newFilm);
        return newFilm;
    }

    @Override
    public Film findFilmById(long id) {
        FilmRowMapper mapper = new FilmRowMapper();
        jdbcTemplate.query("SELECT f.ID film_id,f.NAME,f.DESCRIPTION,f.RELEASEDATE,f.DURATION, " +
                                                    "m.id mpa_id,m.NAME mpa_name,g.ID genre_id,g.NAME genre_name, "  +
                                                 "FROM FILMORATE.FILM f " +
                                                     "LEFT JOIN FILMORATE.МРА m ON f.MPA_ID=m.id " +
                                                     "LEFT JOIN FILMORATE.FILMGENRE fg ON fg.filmid=f.id " +
                                                     "LEFT JOIN FILMORATE.GENRE g ON fg.genreid=g.id where f.id=?", mapper, id);
        List<Film> films = mapper.getResult();
        if (films.size() == 0) {
            return null;
        } else {
            return films.getFirst();
        }
    }

    @Override
    public void addLike(long filmId, long userId) {

        jdbcTemplate.update(
                "INSERT INTO FILMORATE.likes (FilmId, UserId) " +
                        "VALUES (?, ?)",
                filmId, userId);
        log.info("END INSERT Лайк фильм id=" + filmId + " от пользователя id=" + userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        log.info("удалить лайк фильму id=" + filmId + " от пользователя id=" + userId);
        int result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FILMORATE.likes where filmid = ? and UserId = ? ", Integer.class, filmId, userId);
        if (result > 0) {
            jdbcTemplate.update(
                    "delete from FILMORATE.likes where  filmid = ? and UserId = ? ",
                    filmId, userId);
        } else {
            throw new NotFoundException("Лайк пользователя id = " + userId + " не найден");
        }
    }

    @Override
    public Collection<Film> topFilm(int count) {
        log.info(count + " популярных фильмов");
        FilmRowMapper mapper = new FilmRowMapper();
        jdbcTemplate.query("SELECT f.ID film_id,f.NAME,f.DESCRIPTION,f.RELEASEDATE,f.DURATION,m.id mpa_id," +
                        "       m.NAME mpa_name,g.ID genre_id,g.NAME genre_name,l.USERID like_user_id," +
                        "       (SELECT count(*) FROM FILMORATE.LIKES WHERE filmid=f.id) cnt\n" +
                        "                        FROM FILMORATE.FILM f \n" +
                        "                        LEFT JOIN FILMORATE.МРА m ON f.MPA_ID=m.id \n" +
                        "                        LEFT JOIN FILMORATE.LIKES l ON f.id=l.filmid  \n" +
                        "                        LEFT JOIN FILMORATE.FILMGENRE fg ON fg.filmid=f.id \n" +
                        "                        LEFT JOIN FILMORATE.GENRE g ON fg.genreid=g.id \n" +
                        "ORDER BY cnt desc, f.NAME \n" +
                        "LIMIT ? \n",
                mapper, count);
        List<Film> films = mapper.getResult();
        return films;
    }
}
