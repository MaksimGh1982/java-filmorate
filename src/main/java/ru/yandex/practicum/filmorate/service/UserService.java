package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
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
        validate(user);
        return userStorage.create(user);
    }

    public User update(User newUser) {
        log.info("Обновить пользователя id=" + newUser.getId());
        validate(newUser);
        return userStorage.update(newUser);
    }

    public void addFriend(long userId, long friendId) {
        log.info("добавить в друзья пользователю id=" + userId + " пользователя= " + friendId);
        if (userStorage.findUserById(userId) == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        } else if (userStorage.findUserById(friendId) == null) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        } else {
            userStorage.findUserById(userId).getFriends().add(friendId);
            userStorage.findUserById(friendId).getFriends().add(userId);
        }
    }

    public void deleteFriend(long userId, long friendId) {
        log.info("удалить из друзей пользователю id=" + userId + " пользователя= " + friendId);
        if (userStorage.findUserById(userId) == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        } else if (userStorage.findUserById(friendId) == null) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        } else {
            userStorage.findUserById(userId).getFriends().remove(friendId);
            userStorage.findUserById(friendId).getFriends().remove(userId);
        }
    }

    public Collection<User> findFriends(long userId) {
        log.info("Получить друзей пользователя id=" + userId);
        return userStorage.findUserById(userId).getFriends()
                .stream()
                .map(id -> userStorage.findUserById(id))
                .collect(Collectors.toList());
    }

    public Collection<User> findAcrossFriends(long userId, long otherUserId) {
        log.info("Получить пересечение друзей пользователя id=" + userId + " и пользователя id=" + otherUserId);
        return userStorage.findUserById(userId).getFriends()
                .stream()
                .filter(id -> userStorage.findUserById(otherUserId).getFriends().contains(id))
                .map(id -> userStorage.findUserById(id))
                .collect(Collectors.toList());
    }

    private void validate(User user) {
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
