package com.fundynamic.d2tm.math;

public class Random {

    /**
     * Returns value between min *until* max, not including max.
     *
     * Example, if you want a number between 0 or 1, you do: getRandomBetween(0, 2)
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandomBetween(int min, int max) {
        final int maxForRandom = max - min;
        return (min + getInt(maxForRandom));
    }

    public static int getInt(int max) {
        return (int) (Math.random() * max);
    }

    public static float getFloat(float max) {
        return (float) (Math.random() * max);
    }
}
