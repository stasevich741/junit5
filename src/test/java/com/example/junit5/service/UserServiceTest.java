package com.example.junit5.service;

import com.example.junit5.dto.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        userService.add(IVAN, PETR);

        List<User> users = userService.getAll();
//        assertEquals(2, users.size());
        assertThat(users).hasSize(2);
    }

    @AfterEach
    void deleteDataFromDatabase() {
        System.out.println("a@AfterEach " + this);
    }

    @AfterAll
    void closeResources() {
        System.out.println("@AfterAll");
    }

    @Nested
    @Tag("login")
    class LoginTest {
        @Test
        @DisplayName("login fail if user does not exist")
        void loginFailIfUserDoesNotExist() {
            Optional<User> maybeUser = userService.login("Dawon", IVAN.getPassword());

//        assertTrue(maybeUser.isEmpty());
            assertThat(maybeUser).isEmpty();
        }

        @Test
        @DisplayName("login failed if password not correct")
        void loginFailedIfPasswordNotCorrect() {
            userService.add(IVAN);
            Optional<User> maybeUser = userService.login(IVAN.getUsername(), "EBfFe8ED97GxdHEAJUH83m");

//        assertTrue(maybeUser.isEmpty());
            assertThat(maybeUser).isEmpty();
        }

        @Test
        @DisplayName("login success if user exist")
        void loginSuccessIfUserExist() {
            userService.add(IVAN);
            Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());

//        assertTrue(maybeUser.isPresent());
//        maybeUser.ifPresent(user -> assertEquals(IVAN, user));
            assertThat(maybeUser).isPresent();
            maybeUser.ifPresent(user -> assertThat(user).isEqualTo(IVAN));
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

        @ParameterizedTest
//        @ArgumentsSource()
        @NullSource
        @EmptySource
//        @NullAndEmptySource
        @ValueSource(strings =
                {"one", "two", "etc"}
        )
        @DisplayName("login parameterised test")
        void loginParameterisedTest(String username) {
            userService.add(IVAN, PETR);
            Optional<User> maybeUser = userService.login(username, "123");
        }
    }
}
