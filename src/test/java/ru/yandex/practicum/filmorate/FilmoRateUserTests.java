package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
class FilmoRateUserTests {
    private final UserDbStorage userStorage;

    @Test
    public void testCreateUser() {
        User newUser = new User();
        newUser.setName("name1");
        newUser.setLogin("Login1");
        newUser.setEmail("@email1");
        newUser.setBirthday(LocalDate.of(1980,12,12));

        Optional<User> userOptional = Optional.ofNullable(userStorage.create(newUser));

        assertThat(userOptional).isPresent();

    }

    @Test
    public void testFindUserById() {
        User newUser = new User();
        newUser.setName("name1");
        newUser.setLogin("Login1");
        newUser.setEmail("@email1");
        newUser.setBirthday(LocalDate.of(1980,12,12));

        Optional<User> newUserOptional = Optional.ofNullable(userStorage.create(newUser));

        Optional<User> userOptional = Optional.ofNullable(userStorage.findUserById(newUserOptional.get().getId()));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", newUserOptional.get().getId())
                );
    }

    @Test
    public void testUpdateUser() {
        User newUser = new User();
        newUser.setName("name1");
        newUser.setLogin("Login1");
        newUser.setEmail("@email1");
        newUser.setBirthday(LocalDate.of(1980,12,12));

        Optional<User> newUserOptional = Optional.ofNullable(userStorage.create(newUser));

        User upUser = new User();
        upUser.setId(newUserOptional.get().getId());
        upUser.setName("UPname1");
        upUser.setLogin("UPLogin1");
        upUser.setEmail("@UPemail1");
        upUser.setBirthday(LocalDate.of(1980,12,12));

        Optional<User> UpUserOptional = Optional.ofNullable(userStorage.update(upUser));

        Optional<User> userOptional = Optional.ofNullable(userStorage.findUserById(newUserOptional.get().getId()));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "UPname1")
                );
    }
}
