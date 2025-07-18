package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;

@Slf4j
@Repository("UserDbStorage")
public class UserDbStorage implements UserStorage {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public User create(User user) {
        String sqlQuery = "INSERT INTO FILMORATE.USERS ( EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        long userId = keyHolder.getKey().longValue();
        user.setId(userId);

        log.debug("Добавлен пользователь {}", user);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        UserRowMapper mapper = new UserRowMapper();
        return jdbcTemplate.query("SELECT * FROM FILMORATE.USERS", mapper);
    }

    @Override
    public User update(User newUser) {

        jdbcTemplate.update(
                "UPDATE FILMORATE.USERS set email=?, login=?, name=?, birthday=? where id=? ",
                newUser.getEmail(), newUser.getLogin(), newUser.getName(), newUser.getBirthday(), newUser.getId());

        log.debug("Обновлен пользователь", newUser);
        return findUserById(newUser.getId());
    }

    @Override
    public User findUserById(long id) {
        UserRowMapper mapper = new UserRowMapper();
        List<User> users = jdbcTemplate.query("SELECT * FROM FILMORATE.USERS where id = ?", mapper, id);
        if (users.isEmpty()) {
            return null;
        } else {
            return users.getFirst();
        }
    }

    @Override
    public void addFriend(long userId, long friendId) {
        log.info("добавить в друзья пользователю id=" + userId + " пользователя= " + friendId);
        jdbcTemplate.update(
                "INSERT INTO FILMORATE.FRIENDS ( UserId1, UserId2) VALUES (?, ?)",
                userId, friendId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        log.info("удалить из друзей пользователю id=" + userId + " пользователя= " + friendId);
        jdbcTemplate.update(
                "DELETE FROM FILMORATE.FRIENDS where UserId1 = ? and UserId2 = ?", userId, friendId);
    }

    @Override
    public Collection<User> findAcrossFriends(long userId, long otherUserId) {
        UserRowMapper mapper = new UserRowMapper();
        return jdbcTemplate.query("SELECT u.* FROM FILMORATE.FRIENDS f " +
                        "inner join FILMORATE.FRIENDS f1 on f.userId2 = f1.userId2 " +
                        "inner join FILMORATE.USERS u on u.id = f.userId2 " +
                        "where f.userId1 = ? and f1.userId1 = ?",
                mapper, userId, otherUserId);
    }

    @Override
    public Collection<User> findFriends(long userId) {
        UserRowMapper mapper = new UserRowMapper();
        return jdbcTemplate.query("SELECT u.* FROM FILMORATE.FRIENDS f " +
                "inner join FILMORATE.USERS u on u.id = f.userId2" +
                " where f.userId1 = ?", mapper, userId);
    }
}


