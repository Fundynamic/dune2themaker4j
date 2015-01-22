package com.fundynamic.d2tm.game.math;

public class Random {

    public static int getRandomBetween(int min, int max) {
        final int maxForRandom = max - min;
        return (min + getInt(maxForRandom));
    }

    public static int getInt(int max) {
        return (int) (Math.random() * max);
    }

}
