package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    UserController userController;
    User user;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
        user = new User(0,
                "putin@ya.ru",
                "putinRulit",
                "Vova",
                LocalDate.of(1952, 10, 7));
    }

    @Test
    public void shouldThrowExceptionIfEmailWrong() {
        assertAll(
                () -> {
                    user.setEmail(" ");
                    assertThrows(ValidationException.class, () -> userController.createUser(user));
                },
                () -> {
                    user.setEmail("ya.ru");
                    assertThrows(ValidationException.class, () -> userController.createUser(user));
                }
        );
    }

    @Test
    public void shouldThrowExceptionIfLoginIsBlank() {
        assertAll(
                () -> {
                    user.setLogin("");
                    assertThrows(ValidationException.class, () -> userController.createUser(user));
                },
                () -> {
                    user.setEmail("       ");
                    assertThrows(ValidationException.class, () -> userController.createUser(user));
                }
        );
    }

    @Test
    public void shouldRenameIfNameIsBlank() {
        assertAll(
                () -> {
                    user.setName("");
                    assertEquals(user.getLogin(), userController.createUser(user).getName());
                },
                () -> {
                    user.setName("          ");
                    assertEquals(user.getLogin(), userController.changeUser(user).getName());
                }
        );
    }

    @Test
    public void shouldThrowExceptionIfBirthDayIsWrong() {
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }
}
