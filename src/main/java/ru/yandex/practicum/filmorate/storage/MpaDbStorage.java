package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;

@Repository("MpaDbStorage")
@Slf4j
public class MpaDbStorage implements MpaStorage {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Mpa> findAll() {
        MpaRowMapper mapper = new MpaRowMapper();
        return jdbcTemplate.query("SELECT * FROM FILMORATE.МРА", mapper);
    }

    @Override
    public Mpa findMpaById(long id) {
        MpaRowMapper mapper = new MpaRowMapper();
        List<Mpa> mpas = jdbcTemplate.query("SELECT * FROM FILMORATE.МРА where id = ?", mapper, id);

        if (mpas.isEmpty()) {
            return null;
        } else {
            return mpas.getFirst();
        }
    }
}
