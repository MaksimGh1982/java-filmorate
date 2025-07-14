package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {

    private static UserService userService;

    @BeforeAll
    static void beforeAll() {
        userService = new UserService(new InMemoryUserStorage());
    }

    @Test
    void newIncorrectEmailUser() {
        User user = new User();
        user.setEmail("Titanic");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000,12,12));
        user.setName("Titanic");
        assertThrows(ValidationException.class, () -> {
            userService.create(user);
        }, "ValidationException was expected");
    }

    @Test
    void newEmptyUser() {
        User user = new User();
        assertThrows(ValidationException.class, () -> {
            userService.create(user);
        }, "ValidationException was expected");
    }

    @Test
    void newUser() {
        User user = new User();
        user.setEmail("Titanic@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000,12,12));
        user.setName("Titanic");
        userService.create(user);
        assertEquals(1,userService.findAll().size());
    }
}
