package com.fundynamic.d2tm.game;

import static org.junit.Assert.assertEquals;

public class AssertHelper {

    public static void assertFloatEquals(float expected, float actual) {
        assertFloatEquals("", expected, actual);
    }

    public static void assertFloatEquals(String message, float expected, float actual) {
        assertEquals(message, expected, actual, 0.0001F);
    }
}
