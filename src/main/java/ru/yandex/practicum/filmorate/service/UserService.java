package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        log.info("Список пользователей");
        return userStorage.findAll();
    }

    public User findUserById(long id) {
        log.info("Пользователь id=" + id);
        return userStorage.findUserById(id);
    }

    public User create(User user) {
        log.info("Создать пользователя");
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        validate(user);
        return userStorage.create(user);
    }

    public User update(User newUser) {
        log.info("Обновить пользователя id=" + newUser.getId());
        User oldUser = findUserById(newUser.getId());
        if (oldUser != null) {
            validate(newUser);
            return userStorage.update(newUser);
        } else {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }

    }

    public void addFriend(long userId, long friendId) {
        
        if (findUserById(userId) == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        } else if (findUserById(friendId) == null) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        } else {
            userStorage.addFriend(userId, friendId);
        }
    }

    public void deleteFriend(long userId, long friendId) {
        if (findUserById(userId) == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        } else if (findUserById(friendId) == null) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        userStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> findFriends(long userId) {
        if (findUserById(userId) == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        return userStorage.findFriends(userId);
    }

    public Collection<User> findAcrossFriends(long userId, long otherUserId) {

        return userStorage.findAcrossFriends(userId, otherUserId);
    }

    private void validate(User user) {
        if (user == null) {
            throw new ValidationException("Новый пользователь должен быть указан");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Имейл должен быть указан");
            throw new ValidationException("Имейл должен быть указан");
        }

        if (!user.getEmail().contains("@")) {
            log.error("Имейл должен содержать символ @");
            throw new ValidationException("Имейл должен содержать символ @");
        }

        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.error("Логин должен быть указан");
            throw new ValidationException("Логин должен быть указан");
        }

        if (user.getLogin().contains(" ")) {
            log.error("Логин не должен содержать пробел");
            throw new ValidationException("Логин не должен содержать пробел");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
