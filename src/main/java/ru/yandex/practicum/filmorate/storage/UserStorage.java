package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.stream.Collectors;

public interface UserStorage {
    Collection<User> findAll();

    User create(User user);

    User update(User newUser);

    User findUserById(long id);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    public Collection<User> findFriends(long userId);

    public Collection<User> findAcrossFriends(long userId, long otherUserId);
}
