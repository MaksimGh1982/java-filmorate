package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.text.MessageFormat;
import java.util.Collection;

@Service
@Slf4j
@Validated
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
        log.info("Пользователь id = {}", id);
        return userStorage.findUserById(id);
    }

    public User create(@Valid User user) {
        log.info("Создать пользователя");
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    public User update(@Valid User newUser) {
        log.info("Обновить пользователя id = {}", newUser.getId());
        User oldUser = findUserById(newUser.getId());
        if (oldUser != null) {
            return userStorage.update(newUser);
        } else {
            throw new NotFoundException(MessageFormat.format("Пользователь с id = {0} не найден", newUser.getId()));
        }

    }

    public void addFriend(long userId, long friendId) {
        if (findUserById(userId) == null) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id = {0} не найден", userId));
        } else if (findUserById(friendId) == null) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id = {0} не найден", friendId));
        } else {
            userStorage.addFriend(userId, friendId);
        }
    }

    public void deleteFriend(long userId, long friendId) {
        if (findUserById(userId) == null) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id = {0} не найден", userId));
        } else if (findUserById(friendId) == null) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id = {0} не найден", friendId));
        }
        userStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> findFriends(long userId) {
        if (findUserById(userId) == null) {
            throw new NotFoundException(MessageFormat.format("Пользователь с id = {0} не найден", userId));
        }
        return userStorage.findFriends(userId);
    }

    public Collection<User> findAcrossFriends(long userId, long otherUserId) {

        return userStorage.findAcrossFriends(userId, otherUserId);
    }
}
