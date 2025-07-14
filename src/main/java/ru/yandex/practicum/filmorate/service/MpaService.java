package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Service
@Slf4j
public class MpaService {

    @Autowired
    private MpaStorage mpaStorage;

    public Collection<Mpa> findAll() {
        log.info("Список рейтингов");
        return mpaStorage.findAll();
    }

    public Mpa findGenreById(long id) {
        log.info("Рейтинг id=" + id);
        Mpa mpa = mpaStorage.findMpaById(id);
        if (mpa == null) {
            throw new NotFoundException("Рейтинг не найден");
        } else {
            return mpa;
        }
    }
}
