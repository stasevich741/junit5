package com.example.junit5.service;

import com.example.junit5.dto.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@TestInstance(TestInstance.Lifecycle.PER_METHOD)
// по умолчанию (аналог Spring prototype scope)
//@BeforeAll и @AfterAll должны быть статик

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// (аналог Spring Singleton scope) можем убрать статик у @BeforeAll и @AfterAll
class UserServiceTest {

    private static final User PETR = User.of(2, "petr", "111");
    private static final User IVAN = User.of(1, "ivan", "123");
    private UserService userService;

    @BeforeAll
    void init() {
        System.out.println("@BeforeAll");
    }

    @BeforeEach
    void prepare() {
        System.out.println("@BeforeEach " + this);
        userService = new UserService();
    }

    @Test
    void usersEmptyIfNoUsersAdded() {
        System.out.println("test1 " + this);

        List<User> users = userService.getAll();

        //assertFalse(users.isEmpty()); without message
        //if test failed, then message is shown
        assertTrue(users.isEmpty(), "should be empty");
    }

    @Test
    void usersSizeIfUserAdded() {
        System.out.println("test2 " + this);
        userService.add(IVAN);
        userService.add(PETR);

        List<User> users = userService.getAll();
        assertEquals(2, users.size());
    }

    @AfterEach
    void deleteDataFromDatabase() {
        System.out.println("a@AfterEach " + this);
    }

    @Test
    @DisplayName("login success if user exist")
    void loginSuccessIfUserExist() {
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());

        assertTrue(maybeUser.isPresent());
        maybeUser.ifPresent(user -> assertEquals(IVAN, user));
    }

    @AfterAll
    void closeResources() {
        System.out.println("@AfterAll");
    }
}
