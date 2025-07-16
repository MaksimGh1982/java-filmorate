package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    long id;
    @Email(message = "Email должен быть корректным")
    String email;
    @NotBlank(message = "Логин должен быть указан")
    @Pattern(regexp = "^[^ ]+$", message = "Логин не должен содержать пробел")
    String login;
    String name;
    @NotNull(message = "Дата рождения должна быть в прошлом")
    @Past(message = "Дата рождения должна быть в прошлом")
    LocalDate birthday;
    Set<Long> friends;

    public User() {
        friends = new HashSet<>();
    }
}
