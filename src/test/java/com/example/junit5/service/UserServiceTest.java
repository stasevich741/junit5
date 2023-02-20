package com.example.junit5.service;

import com.example.junit5.dto.User;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@TestInstance(TestInstance.Lifecycle.PER_METHOD)
// по умолчанию (аналог Spring prototype scope)
//@BeforeAll и @AfterAll должны быть статик

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// (аналог Spring Singleton scope) можем убрать статик у @BeforeAll и @AfterAll
class UserServiceTest {

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
        userService.add(new User());
        userService.add(new User());

        List<User> users = userService.getAll();
        assertEquals(2, users.size());
    }

    @AfterEach
    void deleteDataFromDatabase() {
        System.out.println("a@AfterEach " + this);
    }

    @AfterAll
    void closeResources() {
        System.out.println("@AfterAll");
    }
}
