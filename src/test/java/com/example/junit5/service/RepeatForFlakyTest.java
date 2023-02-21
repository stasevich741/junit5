package com.example.junit5.service;

import org.junit.jupiter.api.RepeatedTest;

class RepeatForFlakyTest {

    @RepeatedTest(value = 4, name = RepeatedTest.LONG_DISPLAY_NAME)
    void repeatableTest() {
    }
}