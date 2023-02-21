package com.example.junit5.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceExceptionTest {

    private UserService userService;

    @BeforeEach
    void prepare() {
        userService = new UserService();
    }

    @Test
    @DisplayName("throw exception if username or password is null")
    void throwExceptionIfUsernameOrPasswordIsNull() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> userService.login(null, "dummy")),
                () -> {
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.login("dummy", null));
                    assertThat(exception.getMessage()).isEqualTo("username or password is null");
                }
        );

    }
}