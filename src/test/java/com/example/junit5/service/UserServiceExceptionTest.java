package com.example.junit5.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceExceptionTest {

    private UserService userService;

    @BeforeEach
    void prepare() {
        userService = new UserService();
    }


}